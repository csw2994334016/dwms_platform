package com.three.dwms.repository.bm;

import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.entity.bm.Output;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRepository extends PagingAndSortingRepository<Borrow, Integer>, JpaSpecificationExecutor<Borrow> {

    @Query("select max(b.borrowNo) from Borrow b")
    String findMaxBorrowNo();

    List<Borrow> findAllByProposer(@Param("proposer") String proposer);

    List<Borrow> findAllByApprover(String approver);

    List<Borrow> findAllByApproverAndState(String username, int code);

    List<Borrow> findAllByWhCodeIn(List<String> whCodeList);

    Borrow findByBorrowNo(String borrowNo);
}
