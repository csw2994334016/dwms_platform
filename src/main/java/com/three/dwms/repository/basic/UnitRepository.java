package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Category;
import com.three.dwms.entity.basic.Unit;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
public interface UnitRepository extends PagingAndSortingRepository<Unit, Integer> {

    int countByUnitNameAndIdNot(String unitName, Integer id);

    int countByUnitName(String unitName);

    int countByUnitCodeAndIdNot(String unitCode, Integer id);

    int countByUnitCode(String unitCode);
}
