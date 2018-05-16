package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
public interface SysUserRepository extends PagingAndSortingRepository<SysUser, Integer> {

    SysUser findByUsername(String admin);

    int countByUsernameAndIdNot(String username, Integer id);

    int countByUsername(String username);

    int countByEmailAndIdNot(String email, Integer id);

    int countByEmail(String email);

    int countByTelAndIdNot(String tel, Integer id);

    int countByTel(String tel);

    SysUser findByUsernameOrTelOrEmail(String keyword, String keyword1, String keyword2);

    int countByRealNameAndIdNot(String realName, Integer id);

    int countByRealName(String realName);

    List<SysUser> findAllByUsernameContainingOrTelContainingOrEmailContaining(String keyword, String keyword1, String keyword2);

    List<SysUser> findAllBySysRole(SysRole sysRole);
}
