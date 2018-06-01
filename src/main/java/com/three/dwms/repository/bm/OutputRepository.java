package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Output;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutputRepository extends PagingAndSortingRepository<Output, Integer> {

    @Query("select max(o.outputNo) from Output o")
    String findMaxOutputNo();

    List<Output> findAllByProposer(@Param("proposer") String proposer);

    List<Output> findAllByApprover(String approver);

    List<Output> findAllByApproverAndState(String username, int code);

    List<Output> findAllByWhCodeIn(List<String> whCodeList);

    Output findByOutputNo(String outputNo);
}
