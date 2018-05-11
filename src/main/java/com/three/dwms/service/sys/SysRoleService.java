package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.DefaultRole;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysLog;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUserRole;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.repository.sys.SysRoleRepository;
import com.three.dwms.repository.sys.SysUserRepository;
import com.three.dwms.repository.sys.SysUserRoleRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysLogService sysLogService;

    @Transactional
    public void create(RoleParam param) {
        BeanValidator.check(param);
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        if (!DefaultRole.exist(param.getType())) {
            throw new ParamException("角色类型不合法，只存在ADMIN、USER两种角色类型");
        }

        SysRole sysRole = SysRole.builder().name(param.getName()).type(param.getType()).build();

        sysRole.setStatus(param.getStatus());
        sysRole.setRemark(param.getRemark());
        sysRole.setCreator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setCreateTime(new Date());
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());

        sysRole = sysRoleRepository.save(sysRole);
        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_ROLE.getCode()).build();
        sysLogService.saveSysLog(null, sysRole, sysLog);
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        SysRole sysRole = sysRoleRepository.findOne(id);
        Preconditions.checkNotNull(sysRole, "角色不存在");
        sysRole.setStatus(stateCode.getCode());
        sysRoleRepository.save(sysRole);
    }

    @Transactional
    public SysRole update(RoleParam param) {
        BeanValidator.check(param);
        SysRole before = sysRoleRepository.findOne(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        if (!DefaultRole.exist(param.getType())) {
            throw new ParamException("角色类型不合法，只存在ADMIN、USER两种角色类型");
        }

        SysRole after = SysRole.builder().name(param.getName()).type(param.getType()).build();
        after.setId(param.getId());

//        before.setName(param.getName());
//        before.setType(param.getType());

        after.setStatus(param.getStatus());
        after.setRemark(param.getRemark());
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        return sysRoleRepository.save(after);
    }

    public List<SysRole> findAll() {
        return (List<SysRole>) sysRoleRepository.findAll();
    }

    public SysRole findById(int id) {
        SysRole sysRole = sysRoleRepository.findOne(id);
        Preconditions.checkNotNull(sysRole, "角色不存在");
        return sysRole;
    }

    private boolean checkNameExist(String name, Integer id) {
        if (id != null) {
            return sysRoleRepository.countByNameAndIdNot(name, id) > 0;
        }
        return sysRoleRepository.countByName(name) > 0;
    }

    //todo: 角色绑定用户
    public void bindUser(int roleId, List<Integer> userIdList) {
        SysRole sysRole = sysRoleRepository.findOne(roleId);
        Preconditions.checkNotNull(sysRole, "角色绑定用户时，角色不存在");
        for (Integer userId : userIdList) {
            Preconditions.checkNotNull(sysUserRepository.findOne(userId), "角色绑定用户时，待绑定的用户(id:" + userId + ")不存在");
        }
        updateUserRoles(roleId, userIdList);
    }

    @Transactional
    private void updateUserRoles(int roleId, List<Integer> userIdList) {
        //删除该角色原先绑定的用户
        sysUserRoleRepository.deleteByRoleId(roleId);
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (Integer userId : userIdList) {
            SysUserRole sysUserRoleNew = SysUserRole.builder().roleId(roleId).userId(userId).build();
            sysUserRoleNew.setStatus(StateCode.NORMAL.getCode());
            sysUserRoleNew.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysUserRoleNew.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            sysUserRoleNew.setOperateTime(new Date());
            sysUserRoleList.add(sysUserRoleNew);
        }
        //批量插入
        sysUserRoleRepository.save(sysUserRoleList);
    }

    //todo: 角色绑定权限
    public void bindAcl(int roleId, List<Integer> aclIdList) {

    }
}
