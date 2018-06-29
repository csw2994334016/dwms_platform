package com.three.dwms.repository.bm;


import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutputDetailRepository extends PagingAndSortingRepository<OutputDetail, Integer>, JpaSpecificationExecutor<OutputDetail> {
    List<OutputDetail> findAllByOutput(Output output);

    boolean deleteAllByOutput(Output output);

    boolean deleteByOutput(Output output);

    OutputDetail findByOutputAndSku(Output output, String sku);

    int countByOutputAndSku(Output output, String sku);
}
