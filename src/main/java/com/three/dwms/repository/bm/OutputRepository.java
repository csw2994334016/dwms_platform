package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Output;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OutputRepository extends PagingAndSortingRepository<Output, Integer> {
    Output findAllByProposer(String username);

    Output findAllByApprover(String username);

    @Query("select max(o.outputNo) from Output o")
    String findMaxOutputNo();
}
