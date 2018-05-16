package com.three.dwms.repository.basic;

import com.three.dwms.entity.basic.BanJi;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/16.
 * Description:
 */
public interface BanJIRepository extends PagingAndSortingRepository<BanJi, Integer> {
    int countByBanJiCodeAndIdNot(String banJiCode, Integer id);

    int countByBanJiCode(String banJiCode);

    int countByBanJiNameAndIdNot(String banJiName, Integer id);

    int countByBanJiName(String banJiName);
}
