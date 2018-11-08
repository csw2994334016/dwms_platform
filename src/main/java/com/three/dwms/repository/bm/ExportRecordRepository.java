package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.ExportRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
public interface ExportRecordRepository extends PagingAndSortingRepository<ExportRecord, Integer>, JpaSpecificationExecutor<ExportRecord> {

    @Query("select max(e.exportNo) from ExportRecord e")
    String findMaxExportCode();
}
