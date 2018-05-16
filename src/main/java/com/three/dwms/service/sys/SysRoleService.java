package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.*;
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
    private SysUserService sysUserService;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysAclService sysAclService;

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
    public void deleteByIds(List<Integer> ids) {
        List<SysRole> sysRoles = Lists.newArrayList();
        List<SysRoleAcl> sysRoleAcls = Lists.newArrayList();
        for (Integer id : ids) {
            SysRole sysRole = this.findById(id);
//            int count = sysRoleAclRepository.countByRoleId(id);
//            if (count > 0) {
//                throw new ParamException("待删除角色中配有权限，请先删除角色绑定的权限");
//            }
            sysRoles.add(sysRole);
            sysRoleAcls = sysRoleAclRepository.findAllByRoleId(id);
        }
        sysRoleRepository.delete(sysRoles);
        //删除角色绑定的权限信息
        sysRoleAclRepository.delete(sysRoleAcls);
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
    @Transactional
    public void bindUsers(int roleId, List<Integer> userIdList) {
        if (CollectionUtils.isNotEmpty(userIdList)) {
            SysRole sysRole = this.findById(roleId);
            List<SysUser> sysUserList = Lists.newArrayList();
            for (Integer userId : userIdList) {
                SysUser sysUser = sysUserService.findById(userId);
                sysUser.setSysRole(sysRole);
                sysUserList.add(sysUser);
            }
            sysUserRepository.save(sysUserList);
        }
    }

    //角色绑定权限
    @Transactional
    public void bindAcls(int roleId, List<Integer> aclIdList) {
        if (CollectionUtils.isNotEmpty(aclIdList)) {
            SysRole sysRole = this.findById(roleId);
            //删除角色原先绑定的权限
            List<SysRoleAcl> sysRoleAcls = sysRoleAclRepository.findAllByRoleId(roleId);
            sysRoleAclRepository.delete(sysRoleAcls);
            List<SysRoleAcl> sysRoleAclList = new ArrayList<>();
            for (Integer aclId : aclIdList) {
                SysAcl sysAcl = sysAclService.findById(aclId); //权限是否存在
                SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(sysRole.getId()).aclId(sysAcl.getId()).build();
                sysRoleAcl.setStatus(StateCode.NORMAL.getCode());
                sysRoleAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysRoleAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                sysRoleAcl.setOperateTime(new Date());
                sysRoleAclList.add(sysRoleAcl);
            }
            sysRoleAclRepository.save(sysRoleAclList); //批量更新（修改或插入）角色-权限表
        }
    }
}
