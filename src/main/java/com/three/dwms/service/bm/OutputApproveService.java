package com.three.dwms.service.bm;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.OutputStateCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.OutputApproveParam;
import com.three.dwms.repository.bm.OutputDetailRepository;
import com.three.dwms.repository.bm.OutputRepository;
import com.three.dwms.utils.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OutputApproveService {
    @Resource
    private OutputRepository outputRepository;
    @Resource
    private OutputDetailRepository outputDetailRepository;

    //申请通过
    @Transactional
    public void approve(List<OutputApproveParam> outputApproveParamList) {
        List<Output> outputList = Lists.newArrayList();
        if (outputApproveParamList != null && outputApproveParamList.size() > 0) {
            for (int i = 0; i < outputApproveParamList.size(); i++) {
                Output output = outputRepository.findOne(outputApproveParamList.get(i).getId());
                if (output != null) {
                    if (output.getState() == OutputStateCode.APPLY.getCode()) {
                        output.setState(OutputStateCode.APPROVE.getCode());
                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
                        output.setOperateTime(new Date());
                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        output.setRemark(outputApproveParamList.get(i).getRemak());
                        outputList.add(output);
                    } else {
                        throw new ParamException("只有提交申请的单据才可以审批！");
                    }
                } else {
                    throw new ParamException("当前记录不存在！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //审批拒绝
    @Transactional
    public void decline(List<OutputApproveParam> outputApproveParamList) {
        List<Output> outputList = Lists.newArrayList();
        if (outputApproveParamList != null && outputApproveParamList.size() > 0) {
            for (int i = 0; i < outputApproveParamList.size(); i++) {
                Output output = outputRepository.findOne(outputApproveParamList.get(i).getId());
                if (output != null) {
                    if (output.getState() == OutputStateCode.APPLY.getCode()) {
                        output.setState(OutputStateCode.DECLINE.getCode());
                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
                        output.setOperateTime(new Date());
                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        output.setRemark(outputApproveParamList.get(i).getRemak());
                        outputList.add(output);
                    } else {
                        throw new ParamException("只有提交申请的单据才可以审批！");
                    }

                } else {
                    throw new ParamException("当前记录不存在！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //取消审批通过或者拒绝状态
    @Transactional
    public void cancel(List<OutputApproveParam> outputApproveParamList) {
        List<Output> outputList = Lists.newArrayList();
        if (outputApproveParamList != null && outputApproveParamList.size() > 0) {
            for (int i = 0; i < outputApproveParamList.size(); i++) {
                Output output = outputRepository.findOne(outputApproveParamList.get(i).getId());
                if (output != null) {
                    if (output.getState() == OutputStateCode.APPROVE.getCode() || output.getState() == OutputStateCode.DECLINE.getCode()) {
                        output.setState(OutputStateCode.DRAFT.getCode());
                        output.setOperator(RequestHolder.getCurrentUser().getUsername());
                        output.setOperateTime(new Date());
                        output.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        output.setRemark(outputApproveParamList.get(i).getRemak());
                        outputList.add(output);
                    } else {
                        throw new ParamException("只有审批过的单据才可以取消！");
                    }

                } else {
                    throw new ParamException("当前记录不存在！");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //审批单加载
    public List<Output> load() {
        List<Output> outputList = Lists.newArrayList();
//        Output output = outputRepository.findAllByApprover(RequestHolder.getCurrentUser().getUsername());
//        if (output != null) {
//            outputList.add(output);
//        }
        return outputList;
    }
}
