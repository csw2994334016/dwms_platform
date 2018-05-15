package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public interface SysUserRoleRepository extends PagingAndSortingRepository<SysUserRole, Integer> {
    SysUserRole findByRoleIdAndUserId(int roleId, Integer userId);

    @Query(value = "delete from SysUserRole s where s.roleId = ?1")
    void deleteByRoleId(int roleId);

    int countByUserId(Integer id);

    List<SysUserRole> findAllByUserId(Integer userId);
}
