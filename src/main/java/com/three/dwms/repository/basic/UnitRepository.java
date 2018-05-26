package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Unit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
public interface UnitRepository extends PagingAndSortingRepository<Unit, Integer> {

    int countByUnitNameAndIdNot(String unitName, Integer id);

    int countByUnitName(String unitName);

    @Query("select max(u.unitCode) from Unit u")
    String findMaxUnitCode();

    Unit findByUnitName(String unitName);
}
