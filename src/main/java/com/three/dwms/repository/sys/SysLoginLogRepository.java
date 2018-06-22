package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysLoginLog;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/6/22.
 * Description:
 */
public interface SysLoginLogRepository extends PagingAndSortingRepository<SysLoginLog, Integer> {
}
