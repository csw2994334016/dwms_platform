package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Loc;
import org.springframework.data.repository.PagingAndSortingRepository;

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
}
