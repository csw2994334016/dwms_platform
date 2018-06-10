package com.three.dwms.service.sys;

import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysRoleAcl;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.repository.sys.SysAclRepository;
import com.three.dwms.repository.sys.SysRoleAclRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/8.
 * Description:
 */
@Service
public class SysCoreService {

    @Resource
    private SysAclRepository sysAclRepository;

    public boolean hasUrlAcl(SysUser sysUser, String url, String method) {
        if (isSuperAdmin(sysUser)) {
            return true;
        } else {
            //根据url和method查找权限对象
            if ("PUT".equals(method)) {
                url = url.substring(0, url.lastIndexOf("/"));
            }
            SysAcl sysAcl = sysAclRepository.findByUrlAndMethod(url, method);
            if (sysAcl != null) {
                return sysUser.getAlcIdList().contains(sysAcl.getId());
            } else {
                return true;
            }
        }
    }

    private boolean isSuperAdmin(SysUser sysUser) {
        return sysUser.getSysRole().getType().equals(RoleTypeCode.ADMIN.getType());
    }
}
