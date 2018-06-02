package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.OutputStateCode;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.OutputDetailParam;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.repository.bm.OutputDetailRepository;
import com.three.dwms.repository.bm.OutputRepository;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class OutputApplyService {

    @Resource
    private OutputRepository outputRepository;

    @Resource
    private OutputDetailRepository outputDetailRepository;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private OutputService outputService;

    //添加
    @Transactional
    public void create(OutputParam param) {
        BeanValidator.check(param);
        String maxNo = outputRepository.findMaxOutputNo();
        String outputNo = StringUtil.getCurCode("O", maxNo);

        Warehouse warehouse = warehouseService.findByWhName(param.getWhName());

        Output output = Output.builder().outputNo(outputNo).whCode(warehouse.getWhCode()).whName(param.getWhName()).proposer(RequestHolder.getCurrentUser().getUsername()).approver(param.getApprover()).banJiName(param.getBanJiName()).projectName(param.getProjectName()).state(OutputStateCode.DRAFT.getCode()).build();
        output.setRemark(param.getRemark());
        output.setStatus(StatusCode.NORMAL.getCode());
        output.setCreator(RequestHolder.getCurrentUser().getUsername());
        output.setCreateTime(new Date());
        output.setOperator(RequestHolder.getCurrentUser().getUsername());
        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        output.setOperateTime(new Date());

        output = outputRepository.save(output);

        List<OutputDetail> outputDetailList = createDetailList(output, param);
        outputDetailRepository.save(outputDetailList);

    }

    public List<Output> findAll() {
//        return outputService.findAll();
        return outputRepository.findAllByProposer(RequestHolder.getCurrentUser().getUsername());
    }

    @Transactional
    public void cancelByIds(List<Integer> ids) {
        List<Output> outputList = Lists.newArrayList();
        for (Integer id : ids) {
            Output output = outputRepository.findOne(id);
            if (output.getState().equals(OutputStateCode.DRAFT.getCode())) {
                output.setState(OutputStateCode.CANCEL.getCode());
                output.setOperator(RequestHolder.getCurrentUser().getUsername());
                output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                output.setOperateTime(new Date());
                outputList.add(output);
            } else {
                throw new ParamException("只有草稿状态下才可以作废");
            }
        }
        outputRepository.save(outputList);
    }

    @Transactional
    public void update(OutputParam param) {
        BeanValidator.check(param);
        Output output = this.findById(param.getId());
        if (output.getState().equals(OutputStateCode.DRAFT.getCode())) {
            output.setWhName(param.getWhName());
            output.setApprover(param.getApprover());
            output.setBanJiName(param.getBanJiName());
            output.setProjectName(param.getProjectName());
            output.setProposer(RequestHolder.getCurrentUser().getUsername());
            output.setRemark(param.getRemark());
            output.setOperator(RequestHolder.getCurrentUser().getUsername());
            output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            output.setOperateTime(new Date());
            List<OutputDetail> outputDetailList1 = outputDetailRepository.findAllByOutput(output);
            outputDetailRepository.delete(outputDetailList1);
            output = outputRepository.save(output);
            List<OutputDetail> outputDetailList = createDetailList(output, param);
            outputDetailRepository.save(outputDetailList);
        } else {
            throw new ParamException("只有草稿状态下才可以修改");
        }
    }

    private List<OutputDetail> createDetailList(Output output, OutputParam param) {
        List<OutputDetail> outputDetailList = Lists.newArrayList();
        for (OutputDetailParam detailParam : param.getOutputDetailParamList()) {
            BeanValidator.check(detailParam);
            if (existOutputAndSku(output, detailParam.getSku())) {
                throw new ParamException("出库单(outputNo:" + output.getOutputNo() + ")已存在该物料(sku:" + detailParam.getSku() + ")");
            }
            OutputDetail outputDetail = OutputDetail.builder().output(output).sku(detailParam.getSku()).skuDesc(detailParam.getSkuDesc()).spec(detailParam.getSpec()).outNumber(detailParam.getOutNumber()).actualNumber(0.0).build();
            outputDetail.setStatus(StatusCode.NORMAL.getCode());
            outputDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
            outputDetail.setCreateTime(new Date());
            outputDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
            outputDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            outputDetail.setOperateTime(new Date());
            outputDetailList.add(outputDetail);
        }
        return outputDetailList;
    }

    private boolean existOutputAndSku(Output output, String sku) {
        if (output != null && sku != null) {
            return outputDetailRepository.countByOutputAndSku(output, sku) > 0;
        }
        return false;
    }

    private Output findById(Integer id) {
        Output output = outputRepository.findOne(id);
        Preconditions.checkNotNull(output, "出库申请单(id:" + id + ")不存在");
        return output;
    }

    //提交申请
    @Transactional
    public void submitByIds(List<Integer> ids) {
        List<Output> outputList = Lists.newArrayList();
        for (Integer id : ids) {
            Output output = this.findById(id);
            if (output.getState() == OutputStateCode.DRAFT.getCode()) {
                output.setState(OutputStateCode.APPLY.getCode());
                output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                output.setOperateTime(new Date());
                output.setOperator(RequestHolder.getCurrentUser().getUsername());
                outputList.add(output);
            } else {
                throw new ParamException("只有草稿状态下才可以提交申请！");
            }
        }
        outputRepository.save(outputList);
    }

    public List<OutputDetail> findOutputDetails() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("id") != null) {
            Output output = this.findById(Integer.valueOf(request.getParameter("id")));
            return outputDetailRepository.findAllByOutput(output);
        }
        return Lists.newArrayList();
    }

    public List<Output> findCurrentOutputApplies() {
        if (RequestHolder.getCurrentUser() != null) {
            return outputRepository.findAllByProposer(RequestHolder.getCurrentUser().getUsername());
        }
        return Lists.newArrayList();
    }
}
