package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public interface SysUserRoleRepository extends PagingAndSortingRepository<SysUserRole, Integer> {
    SysUserRole findByRoleIdAndUserId(int roleId, Integer userId);

    void deleteByRoleId(int roleId);

    SysUserRole findByUserId(Integer id);

    int countByUserId(Integer id);
}
