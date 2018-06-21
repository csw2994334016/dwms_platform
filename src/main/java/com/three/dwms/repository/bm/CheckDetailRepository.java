package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Check;
import com.three.dwms.entity.bm.CheckDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
public interface CheckDetailRepository extends PagingAndSortingRepository<CheckDetail, Integer> {
    List<CheckDetail> findAllByCheck(Check check);
}
