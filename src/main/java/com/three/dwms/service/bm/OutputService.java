package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.OutputStateCode;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.AllocationParam;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.repository.bm.OutputDetailRepository;
import com.three.dwms.repository.bm.OutputRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.CriteriaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2018/6/1.
 * Description:
 */
@Service
public class OutputService {

    @Resource
    private OutputRepository outputRepository;

    @Resource
    private OutputDetailRepository outputDetailRepository;

    @Resource
    private InventoryRepository inventoryRepository;

    @Resource
    private InventoryService inventoryService;

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
                }
                if (StringUtils.isNotBlank(request.getParameter("banJiName"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("banJiName"), request.getParameter("banJiName")));
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
            outputList = outputRepository.findAll(specification);
        }
        return outputList;
    }

    public List<Inventory> findInventory() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("id") != null) {
            Integer id = Integer.valueOf(request.getParameter("id"));
            Output output = this.findById(id);
            List<OutputDetail> outputDetailList = outputDetailRepository.findAllByOutput(output);
            List<Inventory> inventoryList = Lists.newArrayList();
            for (OutputDetail outputDetail : outputDetailList) {
                List<Inventory> inventories = inventoryRepository.findAllBySkuAndWhName(outputDetail.getSku(), output.getWhName());
                //分配策略
                Double outNumber = outputDetail.getOutNumber() - outputDetail.getActualNumber();
                for (Inventory inventory : inventories) {
                    inventory.setOutputNo(outputDetail.getOutput().getOutputNo());
                    inventory.setOutNumber(outputDetail.getOutNumber());
                    if (outNumber.compareTo(inventory.getSkuAmount()) >= 0) { //申请量比库存量大
                        inventory.setAllocateAmount(inventory.getSkuAmount());
                        outNumber -= inventory.getSkuAmount();
                    } else { //否则按申请量出库
                        inventory.setAllocateAmount(outNumber);
                        outNumber -= outNumber;
                    }
                    inventory.setReturnNumber(0.0);
                }
                inventoryList.addAll(inventories);
            }
            return inventoryList;
        } else {
            return Lists.newArrayList();
        }
    }

    @Transactional
    public void allocate(List<AllocationParam> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            List<Inventory> inventoryList = Lists.newArrayList();
            List<OutputDetail> outputDetailList = Lists.newArrayList();
            List<Output> outputList = Lists.newArrayList();
            for (AllocationParam param : paramList) {
                BeanValidator.check(param);
                Output output = this.findByOutputNo(param.getOutputNo());
                if (output.getState().equals(OutputStateCode.APPROVE.getCode())) {
                    Inventory inventory = inventoryService.findById(param.getId());
                    if (param.getAllocateAmount() > inventory.getSkuAmount()) {
                        throw new ParamException("领用数量大于储位库存数量，无法出库");
                    }
                    if (param.getAllocateAmount() > 0.0) { //出库改变Inventory.skuAmount、OutputDetail.actualNumber、Output.state
                        inventory.setSkuAmount(inventory.getSkuAmount() - param.getAllocateAmount());
                        inventoryList.add(inventory);
                        OutputDetail outputDetail = outputDetailRepository.findByOutputAndSku(output, inventory.getSku());
                        Preconditions.checkNotNull(outputDetail, "物料(outputNo:" + output.getOutputNo() + ", sku:" + inventory.getSku() + ")出库单据详情不存在");
                        if (param.getAllocateAmount() > (outputDetail.getOutNumber() - outputDetail.getActualNumber())) {
                            throw new ParamException("领用数量大于单据申请数量，无法出库");
                        }
                        outputDetail.setActualNumber(outputDetail.getActualNumber() + param.getAllocateAmount());
                        outputDetailList.add(outputDetail);
                        output.setState(OutputStateCode.OUTPUT.getCode());
                        outputList.add(output);
                    }
                } else {
                    throw new ParamException("只有审批通过的单据才能出库");
                }
            }
            inventoryRepository.save(inventoryList);
            outputDetailRepository.save(outputDetailList);
            outputRepository.save(outputList);
        }
    }

    @Transactional
    public void giveBack(List<AllocationParam> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            List<Inventory> inventoryList = Lists.newArrayList();
            List<OutputDetail> outputDetailList = Lists.newArrayList();
            for (AllocationParam param : paramList) {
                BeanValidator.check(param);
                Output output = this.findByOutputNo(param.getOutputNo());
                if (output.getState().equals(OutputStateCode.OUTPUT.getCode())) {
                    Inventory inventory = inventoryService.findById(param.getId());
                    if (param.getReturnNumber() > 0.0) { //退还改变Inventory.skuAmount、OutputDetail.returnNumber
                        inventory.setSkuAmount(inventory.getSkuAmount() + param.getReturnNumber());
                        inventoryList.add(inventory);
                        OutputDetail outputDetail = outputDetailRepository.findByOutputAndSku(output, inventory.getSku());
                        Preconditions.checkNotNull(outputDetail, "物料(outputNo:" + output.getOutputNo() + ", sku:" + inventory.getSku() + ")出库单据详情不存在");
                        outputDetail.setReturnNumber(outputDetail.getReturnNumber() + param.getReturnNumber());
                        outputDetailList.add(outputDetail);
                    }
                } else {
                    throw new ParamException("只有出库状态的单据才能退还");
                }
            }
            inventoryRepository.save(inventoryList);
            outputDetailRepository.save(outputDetailList);
        }
    }

    private Output findByOutputNo(String outputNo) {
        Output output = outputRepository.findByOutputNo(outputNo);
        Preconditions.checkNotNull(output, "出库单(outputNo:" + outputNo + ")不存在");
        return output;
    }

    private Output findById(Integer id) {
        Output output = outputRepository.findOne(id);
        Preconditions.checkNotNull(output, "出库单(id:" + id + ")不存在");
        return output;
    }
}
