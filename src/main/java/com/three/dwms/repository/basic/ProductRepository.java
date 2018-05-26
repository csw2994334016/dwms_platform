package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Category;
import com.three.dwms.entity.basic.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

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

    List<Product> findAllByCategory(Category category);

    Product findBySkuDescAndSpec(String skuDesc, String spec);

    int countBySkuDescAndSpecAndIdNot(String skuDesc, String spec, Integer id);

    int countBySkuDescAndSpec(String skuDesc, String spec);

    @Query("select max(p.sku) from Product p")
    String findMaxSku();
}
