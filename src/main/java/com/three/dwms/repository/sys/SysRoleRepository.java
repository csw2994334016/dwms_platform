package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRole;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public interface SysRoleRepository extends PagingAndSortingRepository<SysRole, Integer> {

    int countByName(String name);

    boolean countByNameAndId(String name, Integer id);

}
