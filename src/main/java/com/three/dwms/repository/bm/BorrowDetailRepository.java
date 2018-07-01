package com.three.dwms.repository.bm;


import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.entity.bm.BorrowDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BorrowDetailRepository extends PagingAndSortingRepository<BorrowDetail, Integer>, JpaSpecificationExecutor<BorrowDetail> {

    List<BorrowDetail> findAllByBorrow(Borrow borrow);

    boolean deleteAllByBorrow(Borrow borrow);

    boolean deleteByBorrow(Borrow borrow);

    BorrowDetail findByBorrowAndSku(Borrow borrow, String sku);

    int countByBorrowAndSku(Borrow borrow, String sku);
}
