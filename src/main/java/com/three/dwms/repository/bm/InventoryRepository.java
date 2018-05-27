package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Inventory;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
public interface InventoryRepository extends PagingAndSortingRepository<Inventory, Integer> {

    Inventory findBySkuAndWhCode(String sku, String whCode);
}
