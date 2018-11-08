package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.InputDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/23.
 * Description:
 */
public interface InputDetailRepository extends PagingAndSortingRepository<InputDetail, Integer>, JpaSpecificationExecutor<InputDetail> {

    @Query("select max(i.inputNo) from InputDetail i")
    String findMaxInputNo();

    int countByBatchNo(String batchNo);

    List<InputDetail> findByIdIn(List<Integer> ids);
}
