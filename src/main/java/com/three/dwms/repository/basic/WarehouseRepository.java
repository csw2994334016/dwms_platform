package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Warehouse;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
public interface WarehouseRepository extends PagingAndSortingRepository<Warehouse, Integer> {
    int countByWhNameAndIdNot(String whName, Integer id);

    int countByWhName(String whName);

    int countByWhCodeAndIdNot(String whCode, Integer id);

    int countByWhCode(String whCode);

    Warehouse findByWhName(String whName);
}
