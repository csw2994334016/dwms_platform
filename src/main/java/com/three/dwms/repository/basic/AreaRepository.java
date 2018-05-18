package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
public interface AreaRepository extends PagingAndSortingRepository<Area, Integer> {
    int countByAreaCodeAndIdNot(Integer areaCode, Integer id);

    int countByAreaCode(Integer areaCode);

    int countByAreaNameAndIdNot(String areaName, Integer id);

    int countByAreaName(String areaName);

    List<Area> findAllByZone(Zone zone);
}
