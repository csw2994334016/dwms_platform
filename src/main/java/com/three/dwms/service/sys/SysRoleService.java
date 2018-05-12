package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysLog;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUserRole;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.param.sys.RoleUserAclParam;
import com.three.dwms.repository.sys.*;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysAclRepository sysAclRepository;

    @Resource
    private SysRoleAclRepository sysRoleAclRepository;

    @Resource
    private SysLogService sysLogService;

    @Transactional
    public void create(RoleUserAclParam param) {
        BeanValidator.check(param);
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        if (!RoleTypeCode.exist(param.getType())) { //扩展功能用
            throw new ParamException("角色类型不合法，只存在ADMIN、USER两种角色类型");
        }

        SysRole sysRole = SysRole.builder().name(param.getName()).type(param.getType()).build();

        sysRole.setStatus(param.getStatus());
        sysRole.setRemark(param.getRemark());

        if (RequestHolder.getCurrentUser() != null) {
            sysRole.setCreator(RequestHolder.getCurrentUser().getUsername());
            sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }
        sysRole.setCreateTime(new Date());
        sysRole.setOperateTime(new Date());

        sysRole = sysRoleRepository.save(sysRole);
        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_ROLE.getCode()).build();
        sysLogService.saveSysLog(null, sysRole, sysLog);

        //创建角色时绑定用户
        bindUsers(sysRole.getId(), param.getUserIds());

        //创建角色时绑定权限
        bindAcls(sysRole.getId(), param.getAclIds());

    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        SysRole sysRole = sysRoleRepository.findOne(id);
        Preconditions.checkNotNull(sysRole, "角色(id:" + id + ")不存在");
        sysRole.setStatus(stateCode.getCode());
        sysRoleRepository.save(sysRole);
    }

    @Transactional
    public SysRole update(RoleParam param) {
        BeanValidator.check(param);
        SysRole before = sysRoleRepository.findOne(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色(id:" + param.getId() + ")不存在");
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        if (!RoleTypeCode.exist(param.getType())) {
            throw new ParamException("角色类型不合法，只存在ADMIN、USER两种角色类型");
        }

        SysRole after = SysRole.builder().name(param.getName()).type(param.getType()).build();
        after.setId(param.getId());

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
        Preconditions.checkNotNull(sysRole, "角色角色(id:" + id + ")不存在");
        return sysRole;
    }

    private boolean checkNameExist(String name, Integer id) {
        if (id != null) {
            return sysRoleRepository.countByNameAndIdNot(name, id) > 0;
        }
        return sysRoleRepository.countByName(name) > 0;
    }

    //角色绑定用户
    public void bindUsers(int roleId, List<Integer> userIdList) {
        SysRole sysRole = this.findById(roleId);
        for (Integer userId : userIdList) {
            updateUserRoles(userId, sysRole.getId());
        }
    }

    @Transactional
    void updateUserRoles(int userId, int roleId) {
        List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findAllByUserId(userId);
        for (SysUserRole sysUserRole : sysUserRoleList) { //如果用户有绑定过角色，查找出来全部更新
            sysUserRole.setRoleId(roleId);
        }
        if (CollectionUtils.isEmpty(sysUserRoleList)) {
            SysUserRole sysUserRoleNew = SysUserRole.builder().roleId(roleId).userId(userId).build();
            sysUserRoleNew.setStatus(StateCode.NORMAL.getCode());
            sysUserRoleNew.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysUserRoleNew.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            sysUserRoleNew.setOperateTime(new Date());
            sysUserRoleList.add(sysUserRoleNew);
        }
        sysUserRoleRepository.save(sysUserRoleList); //批量更新（修改或插入）用户-角色表
    }

    //角色绑定权限
    @Transactional
    public void bindAcls(int roleId, List<Integer> aclIdList) {
        SysRole sysRole = this.findById(roleId);
//        sysRoleAclRepository.deleteByRoleId(roleId);
    }
}
