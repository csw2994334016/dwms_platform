package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Supplier;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/16.
 * Description:
 */
public interface SupplierRepository extends PagingAndSortingRepository<Supplier, Integer> {
    int countBySupplierCodeAndIdNot(String supplierCode, Integer id);

    int countBySupplierCode(String supplierCode);

    int countBySupplierNameAndIdNot(String supplierName, Integer id);

    int countBySupplierName(String supplierName);
}
