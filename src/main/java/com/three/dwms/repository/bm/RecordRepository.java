package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Record;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/6/23.
 * Description:
 */
public interface RecordRepository extends PagingAndSortingRepository<Record, Integer>, JpaSpecificationExecutor<Record> {
}
