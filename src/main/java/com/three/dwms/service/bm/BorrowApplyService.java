package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.BorrowStateCode;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.entity.bm.BorrowDetail;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.*;
import com.three.dwms.repository.bm.BorrowDetailRepository;
import com.three.dwms.repository.bm.BorrowRepository;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.CriteriaUtil;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BorrowApplyService {

    @Resource
    private BorrowRepository borrowRepository;

    @Resource
    private BorrowDetailRepository borrowDetailRepository;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private InventoryService inventoryService;

    //添加
    @Transactional
    public void create(BorrowParam param) {
        BeanValidator.check(param);
        String maxNo = borrowRepository.findMaxBorrowNo();
        String borrowNo = StringUtil.getCurCode("B", maxNo);

        Warehouse warehouse = warehouseService.findByWhName(param.getWhName());

        Borrow borrow = Borrow.builder().borrowNo(borrowNo).whCode(warehouse.getWhCode()).whName(param.getWhName()).proposer(RequestHolder.getCurrentUser().getUsername()).approver(param.getApprover()).returnDate(StringUtil.getStrToDate(param.getReturnDate())).reason(param.getReason()).state(BorrowStateCode.DRAFT.getCode()).build();
        borrow.setRemark(param.getRemark());
        borrow.setStatus(StatusCode.NORMAL.getCode());
        borrow.setCreator(RequestHolder.getCurrentUser().getUsername());
        borrow.setCreateTime(new Date());
        borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
        borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        borrow.setOperateTime(new Date());

//        borrow = borrowRepository.save(borrow);

        List<BorrowDetail> borrowDetailList = createDetailList(borrow, param);
        borrow = borrowRepository.save(borrow);
        for (BorrowDetail borrowDetail : borrowDetailList) {
            borrowDetail.setBorrow(borrow);
        }
        borrowDetailRepository.save(borrowDetailList);
    }

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

    @Transactional
    public void cancelByIds(List<Integer> ids) {
        List<Borrow> borrowList = Lists.newArrayList();
        for (Integer id : ids) {
            Borrow borrow = borrowRepository.findOne(id);
            if (borrow.getState().equals(BorrowStateCode.DRAFT.getCode())) {
                borrow.setState(BorrowStateCode.CANCEL.getCode());
                borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                borrow.setOperateTime(new Date());
                borrowList.add(borrow);
            } else {
                throw new ParamException("只有草稿状态下才可以作废");
            }
        }
        borrowRepository.save(borrowList);
    }

    @Transactional
    public void update(BorrowParam param) {
        BeanValidator.check(param);
        Borrow borrow = this.findById(param.getId());
        if (borrow.getState().equals(BorrowStateCode.DRAFT.getCode())) {
            borrow.setWhName(param.getWhName());
            borrow.setApprover(param.getApprover());
            borrow.setReturnDate(StringUtil.getStrToDate(param.getReturnDate()));
            borrow.setReason(param.getReason());
            borrow.setRemark(param.getRemark());
            borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
            borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            borrow.setOperateTime(new Date());

            borrow = borrowRepository.save(borrow);
            List<BorrowDetail> borrowDetailList = createDetailList(borrow, param);
//            borrow = borrowRepository.save(borrow);
//            for (BorrowDetail borrowDetail : borrowDetailList) {
//                borrowDetail.setBorrow(borrow);
//            }
            borrowDetailRepository.save(borrowDetailList);
        } else {
            throw new ParamException("只有草稿状态下才可以修改");
        }
    }

    private List<BorrowDetail> createDetailList(Borrow borrow, BorrowParam param) {
        List<BorrowDetail> borrowDetailList = Lists.newArrayList();
        for (BorrowDetailParam detailParam : param.getBorrowDetailParamList()) {
            BeanValidator.check(detailParam);
            Inventory inventory = inventoryService.findBySkuAndWhName(detailParam.getSku(), borrow.getWhName());
            if (detailParam.getBorrowNumber() > inventory.getSkuAmount()) {
                throw new ParamException("物料(" + detailParam.getSku() + ")借出数量(" + detailParam.getBorrowNumber() + ")大于库存量(" + inventory.getSkuAmount() + ")");
            }
            BorrowDetail borrowDetail = null;
            if (borrow.getId() != null) {
                borrowDetail = borrowDetailRepository.findByBorrowAndSku(borrow, detailParam.getSku());
            }
//            BorrowDetail borrowDetail = borrowDetailRepository.findByBorrowAndSku(borrow, detailParam.getSku());
            if (borrowDetail == null) {
                borrowDetail = BorrowDetail.builder().borrow(borrow).sku(detailParam.getSku()).skuDesc(detailParam.getSkuDesc()).spec(detailParam.getSpec()).notReturnNumber(detailParam.getBorrowNumber()).returnNumber(0.0).build();
                borrowDetail.setStatus(StatusCode.NORMAL.getCode());
                borrowDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
                borrowDetail.setCreateTime(new Date());
            }
            borrowDetail.setBorrowNumber(detailParam.getBorrowNumber());
            borrowDetail.setNotReturnNumber(borrowDetail.getBorrowNumber());
            borrowDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
            borrowDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            borrowDetail.setOperateTime(new Date());
            borrowDetailList.add(borrowDetail);
        }
        return borrowDetailList;
    }

    private boolean existOutputAndSku(Borrow borrow, String sku) {
        if (borrow != null && sku != null) {
            return borrowDetailRepository.countByBorrowAndSku(borrow, sku) > 0;
        }
        return false;
    }

    private Borrow findById(Integer id) {
        Borrow borrow = borrowRepository.findOne(id);
        Preconditions.checkNotNull(borrow, "借出申请单(id:" + id + ")不存在");
        return borrow;
    }

    //提交申请
    @Transactional
    public void submitByIds(List<Integer> ids) {
        List<Borrow> borrowList = Lists.newArrayList();
        for (Integer id : ids) {
            Borrow borrow = this.findById(id);
            if (borrow.getState() == BorrowStateCode.DRAFT.getCode()) {
                borrow.setState(BorrowStateCode.APPLY.getCode());
                borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                borrow.setOperateTime(new Date());
                borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                borrowList.add(borrow);
            } else {
                throw new ParamException("只有草稿状态下才可以提交申请！");
            }
        }
        borrowRepository.save(borrowList);
    }

    @Transactional
    public void withdrawByIds(List<Integer> ids) {
        List<Borrow> borrowList = Lists.newArrayList();
        for (Integer id : ids) {
            Borrow borrow = this.findById(id);
            if (borrow.getState() == BorrowStateCode.APPLY.getCode()) {
                borrow.setState(BorrowStateCode.DRAFT.getCode());
                borrow.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                borrow.setOperateTime(new Date());
                borrow.setOperator(RequestHolder.getCurrentUser().getUsername());
                borrowList.add(borrow);
            } else {
                throw new ParamException("只有申请状态下才可以撤回申请！");
            }
        }
        borrowRepository.save(borrowList);
    }

    public List<BorrowDetail> findBorrowDetails() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("id") != null) {
            Borrow borrow = this.findById(Integer.valueOf(request.getParameter("id")));
            return borrowDetailRepository.findAllByBorrow(borrow);
        }
        return Lists.newArrayList();
    }

    public List<Borrow> findCurrentBorrowApplies() {
        if (RequestHolder.getCurrentUser() != null) {
            return borrowRepository.findAllByProposer(RequestHolder.getCurrentUser().getUsername());
        }
        return Lists.newArrayList();
    }

    public List<StateCode> findStatus() {
        List<StateCode> stateCodeList = Lists.newArrayList();
        for (BorrowStateCode stateCode : BorrowStateCode.values()) {
            StateCode state = StateCode.builder().code(stateCode.getCode()).desc(stateCode.getDesc()).build();
            stateCodeList.add(state);
        }
        return stateCodeList;
    }

    public void deleteDetailsByIds(List<Integer> ids) {
        List<BorrowDetail> borrowDetailList = Lists.newArrayList();
        for (Integer id : ids) {
            BorrowDetail borrowDetail = borrowDetailRepository.findOne(id);
            Preconditions.checkNotNull(borrowDetail, "借出申请详情(id:" + id + ")不存在");
            borrowDetailList.add(borrowDetail);
        }
        borrowDetailRepository.delete(borrowDetailList);
    }
}
