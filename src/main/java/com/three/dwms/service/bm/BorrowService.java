package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.BorrowStateCode;
import com.three.dwms.entity.bm.*;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.BorrowAllocationParam;
import com.three.dwms.param.statics.Statics;
import com.three.dwms.param.statics.StaticsParam;
import com.three.dwms.repository.bm.BorrowDetailRepository;
import com.three.dwms.repository.bm.BorrowRepository;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by csw on 2018/6/1.
 * Description:
 */
@Service
public class BorrowService {

    @Resource
    private BorrowRepository borrowRepository;

    @Resource
    private BorrowDetailRepository borrowDetailRepository;

    @Resource
    private InventoryRepository inventoryRepository;

    @Resource
    private InventoryService inventoryService;

    public List<Borrow> findAll() {
        List<Borrow> borrowList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Borrow> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("proposer"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("proposer"), request.getParameter("proposer")));
                }
                if (StringUtils.isNotBlank(request.getParameter("approver"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("approver"), request.getParameter("approver")));
                }
                if (StringUtils.isNotBlank(request.getParameter("whCode"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("whCode"), request.getParameter("whCode")));
                }
                String whCodes = RequestHolder.getCurrentUser().getWhCodes();
                if (StringUtils.isBlank(whCodes)) {
                    throw new ParamException("用户没有访问仓库的权限");
                }
                List<String> whCodeList = Arrays.asList(StringUtils.split(whCodes, ","));
                predicateList.add(root.get("whCode").in(whCodeList));
                CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            borrowList = borrowRepository.findAll(specification);
        }
        return borrowList;
    }

    public List<Inventory> findInventory() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("id") != null) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            Borrow borrow = this.findById(id);
            List<BorrowDetail> borrowDetailList = borrowDetailRepository.findAllByBorrow(borrow);
            List<Inventory> inventoryList = Lists.newArrayList();
            for (BorrowDetail borrowDetail : borrowDetailList) {
                List<Inventory> inventories = inventoryRepository.findAllBySkuAndWhNameOrderBySkuAmountAsc(borrowDetail.getSku(), borrow.getWhName());
                //分配策略
                Double outNumber = borrowDetail.getBorrowNumber(); //借出总数量
                Double notReturnNumber = borrowDetail.getNotReturnNumber();
                for (Inventory inventory : inventories) {
                    inventory.setBorrowNo(borrowDetail.getBorrow().getBorrowNo());
                    inventory.setBorrowNumber(borrowDetail.getBorrowNumber()); //该物料总申请量、总借用量
                    if (outNumber.compareTo(inventory.getSkuAmount()) >= 0) { //申请量比库存量大
                        inventory.setAllocateAmount(inventory.getSkuAmount());
                        outNumber -= inventory.getSkuAmount();
                    } else { //否则按申请量出库
                        inventory.setAllocateAmount(outNumber);
                        outNumber -= outNumber;
                    }
                    inventory.setNotReturnNumber(borrowDetail.getNotReturnNumber());
                    inventory.setReturnNumber(notReturnNumber);
                    notReturnNumber -= notReturnNumber;
                }
                inventoryList.addAll(inventories);
            }
            return inventoryList;
        } else {
            return Lists.newArrayList();
        }
    }

    @Transactional
    public void allocate(List<BorrowAllocationParam> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            List<Inventory> inventoryList = Lists.newArrayList();
            List<BorrowDetail> borrowDetailList = Lists.newArrayList();
            List<Borrow> borrowList = Lists.newArrayList();
            for (BorrowAllocationParam param : paramList) {
                BeanValidator.check(param);
                Borrow borrow = this.findByBorrowNo(param.getBorrowNo());
                if (borrow.getState().equals(BorrowStateCode.APPROVE.getCode())) {
                    Inventory inventory = inventoryService.findById(param.getId());
                    if (param.getAllocateAmount() > inventory.getSkuAmount()) {
                        throw new ParamException("领用数量大于储位库存数量，无法出库");
                    }
                    if (param.getAllocateAmount() > 0.0) { //出库改变Inventory.skuAmount、OutputDetail.actualNumber、Output.state
                        inventory.setSkuAmount(inventory.getSkuAmount() - param.getAllocateAmount());
                        inventoryList.add(inventory);
                        BorrowDetail borrowDetail = borrowDetailRepository.findByBorrowAndSku(borrow, inventory.getSku());
                        Preconditions.checkNotNull(borrowDetail, "物料(borrowNo:" + borrow.getBorrowNo() + ", sku:" + inventory.getSku() + ")借出单据详情不存在");
                        if (param.getAllocateAmount() > borrowDetail.getBorrowNumber()) {
                            throw new ParamException("领用数量大于单据申请数量，无法出库");
                        }
                        borrowDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
                        borrowDetail.setOperateTime(new Date());
                        borrowDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                        borrowDetailList.add(borrowDetail);
                        borrow.setState(BorrowStateCode.BORROW.getCode());
                        borrow.setBorrowDate(new Date());
                        borrowList.add(borrow);
                    }
                } else {
                    throw new ParamException("只有审批通过的单据才能出库");
                }
            }
            inventoryRepository.save(inventoryList);
            borrowDetailRepository.save(borrowDetailList);
            borrowRepository.save(borrowList);
        }
    }

    @Transactional
    public void giveBack(List<BorrowAllocationParam> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            List<Inventory> inventoryList = Lists.newArrayList();
            List<BorrowDetail> borrowDetailList = Lists.newArrayList();
            Map<String, BorrowAllocationParam> skuParamMap = new HashMap<>();
            Borrow borrow = this.findByBorrowNo(paramList.get(0).getBorrowNo());
            for (BorrowAllocationParam param : paramList) {
                BeanValidator.check(param);
//                Borrow borrow = this.findByBorrowNo(param.getBorrowNo());
                if (borrow.getState().equals(BorrowStateCode.BORROW.getCode()) || borrow.getState().equals(BorrowStateCode.RETURN_PART.getCode())) {
                    Inventory inventory = inventoryService.findById(param.getId());
                    if (param.getReturnNumber() > 0.0) { //退还改变Inventory.skuAmount、OutputDetail.returnNumber
                        inventory.setSkuAmount(inventory.getSkuAmount() + param.getReturnNumber());
                        inventoryList.add(inventory);
                        skuParamMap.putIfAbsent(inventory.getSku(), BorrowAllocationParam.builder().borrowNo(param.getBorrowNo()).sku(inventory.getSku()).allReturnNumber(0.0).build());
                        skuParamMap.get(inventory.getSku()).setAllReturnNumber(skuParamMap.get(inventory.getSku()).getAllReturnNumber() + param.getReturnNumber());
                    }
                } else {
                    throw new ParamException("只有出库状态的单据才能退还");
                }
            }
            boolean returnFlag = true;
            for (Map.Entry<String, BorrowAllocationParam> entry : skuParamMap.entrySet()) {
//                Borrow borrow = this.findByBorrowNo(entry.getValue().getBorrowNo());
                BorrowDetail borrowDetail = borrowDetailRepository.findByBorrowAndSku(borrow, entry.getValue().getSku());
                Preconditions.checkNotNull(borrowDetail, "物料(borrowNo:" + borrow.getBorrowNo() + ", sku:" + entry.getValue().getSku() + ")借出单据详情不存在");
                borrowDetail.setReturnNumber(borrowDetail.getReturnNumber() + entry.getValue().getAllReturnNumber());
                borrowDetail.setNotReturnNumber(borrowDetail.getBorrowNumber() - borrowDetail.getReturnNumber());
                borrowDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
                borrowDetail.setOperateTime(new Date());
                borrowDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                borrowDetailList.add(borrowDetail);
                if (borrowDetail.getNotReturnNumber() > 0.0) {
                    returnFlag = false;
                }
            }
            borrow.setActualReturnDate(new Date());
            borrow.setState(returnFlag ? BorrowStateCode.RETURN.getCode() : BorrowStateCode.RETURN_PART.getCode());
            inventoryRepository.save(inventoryList);
            borrowDetailRepository.save(borrowDetailList);
            borrowRepository.save(borrow);
        }
    }

    private Borrow findByBorrowNo(String borrowNo) {
        Borrow borrow = borrowRepository.findByBorrowNo(borrowNo);
        Preconditions.checkNotNull(borrow, "借出申请单(borrowNo:" + borrowNo + ")不存在");
        return borrow;
    }

    private Borrow findById(Integer id) {
        Borrow borrow = borrowRepository.findOne(id);
        Preconditions.checkNotNull(borrow, "借出申请单(id:" + id + ")不存在");
        return borrow;
    }

    public Statics borrowStatics(StaticsParam param) {
        String startTime = StringUtil.getStartTime(param.getYear(), param.getMonth());
        String endTime = StringUtil.getEndTime(param.getYear(), param.getMonth());
        List<BorrowDetail> borrowDetailList = Lists.newArrayList();
        Specification<BorrowDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            if (StringUtils.isNotBlank(param.getSku())) {
                predicateList.add(criteriaBuilder.equal(root.get("sku"), param.getSku()));
            }
            Date st = StringUtil.getStrToDate(startTime);
            Date et = StringUtil.getStrToDate(endTime);
            if (st != null && et != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("operateTime"), st));
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("operateTime"), et));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        borrowDetailList.addAll(borrowDetailRepository.findAll(specification));
        Map<String, Double> dayNumberMap = Maps.newHashMap();
        for (BorrowDetail borrowDetail : borrowDetailList) {
            if (borrowDetail.getOperateTime() != null) {
                String operateTimeStr = StringUtil.getDateToStr(borrowDetail.getOperateTime());
                String day = operateTimeStr.split("-")[2];
                dayNumberMap.putIfAbsent(day, 0.0);
                dayNumberMap.put(day, CalculateUtil.add(dayNumberMap.get(day), borrowDetail.getBorrowNumber()));
            }
        }
        Statics statics = Statics.builder().labelList(Lists.newArrayList()).dataList(Lists.newArrayList()).build();
        return CriteriaUtil.createStatics(startTime, endTime, dayNumberMap, statics);
    }
}
