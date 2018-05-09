package com.three.dwms.service.sys;

import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.DefaultRole;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.repository.sys.SysRoleRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Transactional
    public void create(RoleParam param) {
        BeanValidator.check(param);
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        if (!DefaultRole.exist(param.getType())) {
            throw new ParamException("角色类型不合法，只存在ROLE_ADMIN、ROLE_USER两种类型");
        }

        SysRole sysRole = SysRole.builder().name(param.getName()).type(param.getType()).build();

        sysRole.setStatus(param.getStatus());
        sysRole.setRemark(param.getRemark());
        sysRole.setCreator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setCreateTime(new Date());
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());

        sysRoleRepository.save(sysRole);

    }

    private boolean checkNameExist(String name, Integer id) {
        if (id != null) {
            return sysRoleRepository.countByNameAndId(name, id);
        }
        return sysRoleRepository.countByName(name) > 0;
    }
}
