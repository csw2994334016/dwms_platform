package com.three.dwms.service.bm;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.OutputStateCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.OutputDetailParam;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.repository.bm.OutputDetailRepository;
import com.three.dwms.repository.bm.OutputRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OutputApplyService {

    @Resource
    private OutputRepository outputRepository;

    @Resource
    private OutputDetailRepository outputDetailRepository;

    //添加
    @Transactional
    public void create(OutputParam param) {
        BeanValidator.check(param);
        String maxNo = outputRepository.findMaxOutputNo();
        String outputNo = StringUtil.getCurCode("O", maxNo);
        Output output = Output.builder().outputNo(outputNo).whName(param.getWhName()).proposer(RequestHolder.getCurrentUser().getUsername()).approver(param.getApprover()).banJiName(param.getBanJiName()).projectName(param.getProjectName()).state(OutputStateCode.DRAFT.getCode()).build();
        output.setRemark(param.getRemark());
        output.setStatus(StateCode.NORMAL.getCode());
        output.setCreator(RequestHolder.getCurrentUser().getUsername());
        output.setCreateTime(new Date());
        output.setOperator(RequestHolder.getCurrentUser().getUsername());
        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        output.setOperateTime(new Date());

        output = outputRepository.save(output);

        List<OutputDetail> outputDetailList = Lists.newArrayList();
        for (OutputDetailParam detailParam : param.getOutputDetailParamList()) {
            BeanValidator.check(detailParam);
            OutputDetail outputDetail = OutputDetail.builder().output(output).sku(detailParam.getSku()).skuDesc(detailParam.getSkuDesc()).spec(detailParam.getSpec()).outNumber(detailParam.getOutNumber()).build();
            outputDetail.setStatus(StateCode.NORMAL.getCode());
            outputDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
            outputDetail.setCreateTime(new Date());
            outputDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
            outputDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            outputDetail.setOperateTime(new Date());
            outputDetailList.add(outputDetail);
        }
        outputDetailRepository.save(outputDetailList);

    }

    public List<Output> findAll() {
        return (List<Output>) outputRepository.findAll();
    }

//    //更新
//    @Transactional
//    public void update(List<OutputParam> paramList) {
//        List<Output> outputList = Lists.newArrayList();
//        List<OutputDetail> outputDetailList = Lists.newArrayList();
//        BeanValidator.check(paramList);
//        if (paramList != null && paramList.size() > 0) {
//            for (int i = 0; i < paramList.size(); i++) {
//                Output output = outputRepository.findOne(paramList.get(i).getId());
//                if (output != null) {
//                    if (output.getState() == OutputStateCode.DRAFT.getCode()) {
//                        output.setApprover(paramList.get(i).getApprover());
//                        output.setBanJiName(paramList.get(i).getBanJiName());
//                        output.setProjectName(paramList.get(i).getProjectName());
//                        output.setProposer(RequestHolder.getCurrentUser().getUsername());
//                        output.setWhName(paramList.get(i).getWhName());
//                        output.setCreateTime(new Date());
//                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//                        output.setOperateTime(new Date());
//                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
//                        output.setCreator(RequestHolder.getCurrentUser().getUsername());
//                        output.setRemark(paramList.get(i).getRemark());
//                        output.setStatus(StateCode.NORMAL.getCode());
//
//                        if (paramList.get(i).getOutputDetailParamList() != null && paramList.get(i).getOutputDetailParamList().size() > 0) {
//
//                            List<OutputDetailParam> ODPList = paramList.get(i).getOutputDetailParamList();
//                            for (int j = 0; j < ODPList.size(); j++) {
//                                OutputDetail outputDetail = outputDetailRepository.findOne(ODPList.get(j).getId());
//                                outputDetail.setOutNumber(ODPList.get(j).getOutNumber());
//                                outputDetail.setReturnNumber(ODPList.get(j).getReturnNumber());
//                                outputDetail.setSku(ODPList.get(j).getSku());
//                                outputDetail.setSkuDesc(ODPList.get(j).getSkuDesc());
//                                outputDetail.setSpec(ODPList.get(j).getSpec());
//                                outputDetail.setCreateTime(new Date());
//                                outputDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
//                                outputDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//                                outputDetail.setOperateTime(new Date());
//                                outputDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
//                                outputDetail.setRemark(ODPList.get(j).getRemark());
//                                outputDetail.setStatus(StateCode.NORMAL.getCode());
//                                outputDetailList.add(outputDetail);
//                            }
//                        }
//                        outputList.add(output);
//
//                    } else {
//                        throw new ParamException("只有草稿状态下才可以修改！");
//                    }
//                } else {
//                    throw new ParamException("当前记录不存在！");
//                }
//            }
//            outputDetailRepository.save(outputDetailList);
//            outputRepository.save(outputList);
//
//        }
//    }
//
//    //作废
//    @Transactional
//    public void cancle(List<OutputParam> paramList) {
//        List<Output> outputList = Lists.newArrayList();
//        if (paramList != null && paramList.size() > 0) {
//            for (int i = 0; i < paramList.size(); i++) {
//                Output output = outputRepository.findOne(paramList.get(i).getId());
//                if (output != null) {
//                    if (output.getState() == OutputStateCode.DRAFT.getCode()) {
//                        output.setState(OutputStateCode.CANCEL.getCode());
//                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
//                        output.setOperateTime(new Date());
//                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//                        outputList.add(output);
//                    } else {
//                        throw new ParamException("只有草稿状态下才可以作废！");
//                    }
//                } else {
//                    throw new ParamException("当前记录不存在！");
//                }
//            }
//            outputRepository.save(outputList);
//        }
//
//    }
//
//
//
//    //提交申请
//    @Transactional
//    public void submit(List<OutputParam> paramList) {
//        List<Output> outputList = Lists.newArrayList();
//        List<OutputDetail> outputDetailList = Lists.newArrayList();
//        BeanValidator.check(paramList);
//        if (paramList != null && paramList.size() > 0) {
//            for (int i = 0; i < paramList.size(); i++) {
//                Output output = outputRepository.findOne(paramList.get(i).getId());
//                if (output != null) {
//                    if (output.getState() == OutputStateCode.DRAFT.getCode()) {
//
//                        output.setState(OutputStateCode.APPLY.getCode());
//                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//                        output.setOperateTime(new Date());
//                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
//
//
//                    } else {
//                        throw new ParamException("只有草稿状态下才可以提交申请！");
//                    }
//                } else {
//                    throw new ParamException("当前记录不存在！");
//                }
//            }
//
//            outputRepository.save(outputList);
//
//        }
//    }
//
//    //加载
//    public List<Output> load() {
//        List<Output> outputList = Lists.newArrayList();
//        Output output = outputRepository.findAllByProposer(RequestHolder.getCurrentUser().getUsername());
//        if (output != null) {
//            outputList.add(output);
//        }
//        return outputList;
//    }
}
