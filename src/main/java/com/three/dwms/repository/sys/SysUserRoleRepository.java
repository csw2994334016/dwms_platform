package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUserRole;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public interface SysUserRoleRepository extends PagingAndSortingRepository<SysUserRole, Integer> {
    SysUserRole findByRoleIdAndUserId(int roleId, Integer userId);

    void deleteByRoleId(int roleId);

    int countByUserId(Integer id);

    List<SysUserRole> findAllByUserId(Integer userId);
}
