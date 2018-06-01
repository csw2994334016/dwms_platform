package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.InventoryParam;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.utils.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
@Service
public class InventoryService {

    @Resource
    private InventoryRepository inventoryRepository;

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
}
