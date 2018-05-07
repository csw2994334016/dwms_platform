package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysUser;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
public interface SysUserRepository extends PagingAndSortingRepository<SysUser, Integer> {

    SysUser findByUsername(String admin);
}
