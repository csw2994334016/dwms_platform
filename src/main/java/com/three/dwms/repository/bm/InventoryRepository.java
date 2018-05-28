package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
public interface InventoryRepository extends PagingAndSortingRepository<Inventory, Integer> {

    Inventory findBySkuAndWhCode(String sku, String whCode);

    @Query("select i.whName, i.sku, i.skuDesc, i.spec, sum(i.skuAmount) from Inventory i where i.whName = :whName group by i.whName, i.sku, i.skuDesc, i.spec")
    List<Object[]> findAllByWhName(@Param("whName") String whName);

    Inventory findBySkuAndWhCodeAndLocName(String sku, String whCode, String locName);
}
