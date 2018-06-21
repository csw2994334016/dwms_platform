package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Check;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
public interface CheckRepository extends PagingAndSortingRepository<Check, Integer>, JpaSpecificationExecutor<Check> {

    @Query("select max(c.checkNo) from Check c")
    String findMaxCheckNo();
}
