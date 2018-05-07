package com.three.dwms.service.sys;

import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.repository.sys.SysUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@Service
public class SysUserService {

    @Resource
    private SysUserRepository sysUserRepository;

    public List<SysUser> getAll() {
        SysUser sysUser = sysUserRepository.findByUsername("admin");
        return (List<SysUser>) sysUserRepository.findAll();
    }
}
