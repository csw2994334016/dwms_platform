package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    int countBySkuDescAndIdNot(String skuDesc, Integer id);

    int countBySkuDesc(String skuDesc);

    int countBySkuAndIdNot(String sku, Integer id);

    int countBySku(String sku);

    int countByCategoryId(int id);
}
