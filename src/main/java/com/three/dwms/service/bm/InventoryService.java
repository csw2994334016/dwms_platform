package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.entity.basic.Loc;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.InventoryParam;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.service.basic.LocService;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.utils.BeanValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
@Service
public class InventoryService {

    @Resource
    private InventoryRepository inventoryRepository;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private LocService locService;

    public List<Inventory> findByWhName(InventoryParam param) {
        BeanValidator.check(param);
        List<Object[]> objectList = inventoryRepository.findAllByWhName(param.getWhName());
        List<Inventory> inventoryList = Lists.newArrayList();
        for (Object[] o : objectList) {
            Inventory inventory = Inventory.builder().whName((String) o[0]).sku((String) o[1]).skuDesc((String) o[2]).spec((String) o[3]).skuAmount((Double) o[4]).build();
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    public Inventory findById(Integer id) {
        Inventory inventory = inventoryRepository.findOne(id);
        Preconditions.checkNotNull(inventory, "库存汇总信息(id:" + id + ")不存在");
        return inventory;
    }

    public List<Inventory> findAll() {
        List<Inventory> inventoryList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Inventory> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("whId"))) {
                    Warehouse warehouse = warehouseService.findById(Integer.valueOf(request.getParameter("whId")));
                    predicateList.add(criteriaBuilder.equal(root.get("whCode"), warehouse.getWhCode()));
                }
                if (StringUtils.isNotBlank(request.getParameter("locId"))) {
                    Loc loc = locService.findById(Integer.valueOf(request.getParameter("locId")));
                    predicateList.add(criteriaBuilder.equal(root.get("locName"), loc.getLocName()));
                }
                if (StringUtils.isNotBlank(request.getParameter("sku"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("sku"), request.getParameter("sku")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            inventoryList = inventoryRepository.findAll(specification);
        }
        return inventoryList;
    }
}
