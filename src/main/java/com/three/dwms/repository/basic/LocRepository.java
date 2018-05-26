package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Loc;
import com.three.dwms.entity.basic.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
public interface LocRepository extends PagingAndSortingRepository<Loc, Integer> {
    int countByLocCodeAndIdNot(Integer locCode, Integer id);

    int countByLocCode(Integer locCode);

    int countByLocNameAndIdNot(String locName, Integer id);

    int countByLocName(String locName);

    List<Loc> findAllByArea(Area area);

    @Query("select max(l.locCode) from Loc l where l.area = :area")
    Integer findMaxLocCodeByArea(@Param("area") Area area);

    @Query("select l from Loc l where l.area.zone.warehouse = :warehouse")
    List<Loc> findAllByWarehouse(@Param("warehouse") Warehouse warehouse);
}
