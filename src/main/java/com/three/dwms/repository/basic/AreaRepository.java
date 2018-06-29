package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Zone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
public interface AreaRepository extends PagingAndSortingRepository<Area, Integer> {

    int countByAreaNameAndIdNot(String areaName, Integer id);

    int countByAreaName(String areaName);

    List<Area> findAllByZoneOrderByAreaNameAsc(Zone zone);

    int countByAreaNameAndZoneAndIdNot(String areaName, Zone zone, Integer id);

    int countByAreaNameAndZone(String areaName, Zone zone);

    @Query("select max(a.areaCode) from Area a where a.zone = :zone")
    Integer findMaxAreaCodeByZone(@Param("zone") Zone zone);

    List<Area> findAllByZone(Zone zone);
}
