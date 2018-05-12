package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRoleAcl;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/12.
 * Description:
 */
public interface SysRoleAclRepository extends PagingAndSortingRepository<SysRoleAcl, Integer> {

    List<SysRoleAcl> findAllByRoleId(Integer id);
}
