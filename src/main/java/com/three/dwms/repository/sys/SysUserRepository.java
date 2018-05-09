package com.three.dwms.repository.sys;

import com.three.dwms.entity.sys.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
public interface SysUserRepository extends PagingAndSortingRepository<SysUser, Integer> {

    SysUser findByUsername(String admin);

//    @Query(value = "select count(1) from sys_user user where user.username=?1 and user.id!=?2", nativeQuery = true)
    int countByUsernameAndIdNot(String username, Integer id);

    int countByUsername(String username);

    int countByEmailAndIdNot(String email, Integer id);

    int countByEmail(String email);

    int countByTelAndIdNot(String tel, Integer id);

    int countByTel(String tel);

    SysUser findByUsernameOrTelOrEmail(String keyword, String keyword1, String keyword2);
}
