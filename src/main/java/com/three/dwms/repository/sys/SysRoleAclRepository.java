package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRoleAcl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by csw on 2018/5/12.
 * Description:
 */
public interface SysRoleAclRepository extends PagingAndSortingRepository<SysRoleAcl, Integer> {

    List<SysRoleAcl> findAllByRoleId(Integer id);

    void deleteByRoleId(@Param("roleId") int roleId);

    int countByRoleId(Integer id);

    int countByRoleIdAndAclId(Integer id, Integer id1);
}
