package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
public interface InventoryRepository extends PagingAndSortingRepository<Inventory, Integer> {

    Inventory findBySkuAndWhCode(String sku, String whCode);

    @Query("select i from Inventory i ")
    List<Inventory> findAllByWhName(String whName);

    Inventory findBySkuAndWhCodeAndLocName(String sku, String whCode, String locName);
}
