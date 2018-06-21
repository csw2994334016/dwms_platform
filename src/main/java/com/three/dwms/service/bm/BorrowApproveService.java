package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.BorrowStateCode;
import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.BorrowParam;
import com.three.dwms.repository.bm.BorrowRepository;
import com.three.dwms.utils.CriteriaUtil;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BorrowApproveService {

    @Resource
    private BorrowRepository borrowRepository;

    //申请通过
    @Transactional
    public void approve(List<BorrowParam> paramList) {
        List<Borrow> outputList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (BorrowParam param : paramList) {
                Borrow borrow = this.findById(param.getId());
                if (borrow.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
                    if (borrow.getState() == BorrowStateCode.APPLY.getCode()) {
                        borrow.setState(BorrowStateCode.APPROVE.getCode());
                        borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                        borrow.setOperateTime(new Date());
                        borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        borrow.setRemark(param.getRemark());
                        outputList.add(borrow);
                    } else {
                        throw new ParamException("只有提交申请的单据才可以通过审批！");
                    }
                } else {
                    throw new ParamException("审批人非当前用户");
                }
            }
            borrowRepository.save(outputList);
        }
    }

    //审批拒绝
    @Transactional
    public void decline(List<BorrowParam> paramList) {
        List<Borrow> borrowList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (BorrowParam param : paramList) {
                Borrow borrow = this.findById(param.getId());
                if (borrow.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
                    if (borrow.getState() == BorrowStateCode.APPLY.getCode()) {
                        borrow.setState(BorrowStateCode.DECLINE.getCode());
                        borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                        borrow.setOperateTime(new Date());
                        borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        borrow.setRemark(param.getRemark());
                        borrowList.add(borrow);
                    } else {
                        throw new ParamException("只有提交申请的单据才可以拒绝审批！");
                    }
                } else {
                    throw new ParamException("审批人非当前用户");
                }
            }
            borrowRepository.save(borrowList);
        }
    }

    //取消审批通过或者拒绝状态
    @Transactional
    public void cancel(List<BorrowParam> paramList) {
        List<Borrow> borrowList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramList)) {
            for (BorrowParam param : paramList) {
                Borrow borrow = this.findById(param.getId());
                if (borrow.getApprover().equals(RequestHolder.getCurrentUser().getUsername())) {
                    if (borrow.getState() == BorrowStateCode.APPROVE.getCode() || borrow.getState() == BorrowStateCode.DECLINE.getCode()) {
                        borrow.setState(BorrowStateCode.APPLY.getCode());
                        borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                        borrow.setOperateTime(new Date());
                        borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        borrow.setRemark(param.getRemark());
                        borrowList.add(borrow);
                    } else {
                        throw new ParamException("只有审批通过或者拒绝状态的单据才可以取消！");
                    }
                } else {
                    throw new ParamException("审批人非当前用户");
                }
            }
            borrowRepository.save(borrowList);
        }
    }

    //审批单加载
    public List<Borrow> findAll() {
        List<Borrow> borrowList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Borrow> specification = (root, criteriaQuery, criteriaBuilder) -> {
                return CriteriaUtil.getPredicate(request, criteriaBuilder, root.get("proposer"), root.get("approver"), root.get("state"), root.get("createTime"));
            };
            borrowList = borrowRepository.findAll(specification);
        }
        return borrowList;
    }

    private Borrow findById(Integer id) {
        Borrow borrow = borrowRepository.findOne(id);
        Preconditions.checkNotNull(borrow, "借出申请单(id:" + id + ")不存在");
        return borrow;
    }
}
