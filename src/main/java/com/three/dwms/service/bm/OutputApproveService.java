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
import com.three.dwms.utils.TimeCriteriaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
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
                if (output.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
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
                } else {
                    throw new ParamException("审批人非当前用户");
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
                if (output.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
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
                } else {
                    throw new ParamException("审批人非当前用户");
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
                if (output.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
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
                } else {
                    throw new ParamException("审批人非当前用户");
                }
            }
            outputRepository.save(outputList);
        }
    }

    //审批单加载
    public List<Output> findAll() {
        List<Output> outputList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Output> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("proposer"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("proposer"), request.getParameter("proposer")));
                }
                if (StringUtils.isNotBlank(request.getParameter("approver"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("approver"), request.getParameter("approver")));
                } else {
                    predicateList.add(criteriaBuilder.equal(root.get("approver"), RequestHolder.getCurrentUser().getUsername()));
                }
                if (StringUtils.isNotBlank(request.getParameter("state"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("state"), Integer.valueOf(request.getParameter("state"))));
                }
                TimeCriteriaUtil.timePredication(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            outputList = outputRepository.findAll(specification);
        }
        return outputList;
    }

    private Output findById(Integer id) {
        Output output = outputRepository.findOne(id);
        Preconditions.checkNotNull(output, "出库单(id:" + id + ")不存在");
        return output;
    }
}
