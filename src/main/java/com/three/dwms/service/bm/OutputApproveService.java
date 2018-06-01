package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.OutputStateCode;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.repository.bm.OutputRepository;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OutputApproveService {

    @Resource
    private OutputRepository outputRepository;

    //申请通过
    @Transactional
    public void approve(List<OutputParam> paramList) {
        List<Output> outputList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (OutputParam param : paramList) {
                Output output = this.findById(param.getId());
                if (output.getState() == OutputStateCode.APPLY.getCode()) {
                    output.setState(OutputStateCode.APPROVE.getCode());
                    output.setOperator(RequestHolder.getCurrentUser().getUsername());
                    output.setOperateTime(new Date());
                    output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    output.setRemark(param.getRemark());
                    outputList.add(output);
                } else {
                    throw new ParamException("只有提交申请的单据才可以审批！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //审批拒绝
    @Transactional
    public void decline(List<OutputParam> paramList) {
        List<Output> outputList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (OutputParam param : paramList) {
                Output output = this.findById(param.getId());
                if (output.getState() == OutputStateCode.APPLY.getCode()) {
                    output.setState(OutputStateCode.DECLINE.getCode());
                    output.setOperator(RequestHolder.getCurrentUser().getUsername());
                    output.setOperateTime(new Date());
                    output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    output.setRemark(param.getRemark());
                    outputList.add(output);
                } else {
                    throw new ParamException("只有提交申请的单据才可以审批！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //取消审批通过或者拒绝状态
    @Transactional
    public void cancel(List<OutputParam> paramList) {
        List<Output> outputList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (OutputParam param : paramList) {
                Output output = this.findById(param.getId());
                if (output.getState() == OutputStateCode.APPROVE.getCode() || output.getState() == OutputStateCode.DECLINE.getCode()) {
                    output.setState(OutputStateCode.DRAFT.getCode());
                    output.setOperator(RequestHolder.getCurrentUser().getUsername());
                    output.setOperateTime(new Date());
                    output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    output.setRemark(param.getRemark());
                    outputList.add(output);
                } else {
                    throw new ParamException("只有审批通过的单据才可以取消！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //审批单加载
    public List<Output> findAll() {
        return outputRepository.findAllByApprover(RequestHolder.getCurrentUser().getUsername());
    }

    private Output findById(Integer id) {
        Output output = outputRepository.findOne(id);
        Preconditions.checkNotNull(output, "出库单(id:" + id + ")不存在");
        return output;
    }
}
