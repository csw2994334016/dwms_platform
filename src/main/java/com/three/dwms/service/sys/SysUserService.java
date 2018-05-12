package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.*;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.UserRoleAcl;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.repository.sys.SysRoleAclRepository;
import com.three.dwms.repository.sys.SysUserRepository;
import com.three.dwms.repository.sys.SysUserRoleRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@Service
@Slf4j
public class SysUserService {

    @Value("#{props['init.username']}")
    private String username;

    @Value("#{props['init.defaultPassword']}")
    private String defaultPassword;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysAclService sysAclService;

    @Resource
    private SysRoleAclRepository sysRoleAclRepository;

    @Transactional
    public void create(UserParam param) {
        BeanValidator.check(param);
        if (checkUsernameExist(param.getUsername(), param.getId())) {
            throw new ParamException("用户名已经存在");
        }
//        if (checkRealNameExist(param.getRealName(), param.getId())) {
//            throw new ParamException("用户真实姓名已经存在");
//        }
        if (checkTelExist(param.getTel(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        //查找角色
        SysRole sysRole = sysRoleService.findById(param.getRoleId());

        //密码为空，初始化默认密码
        String password = StringUtils.isBlank(param.getPassword()) ? defaultPassword : param.getPassword();
        password = MD5Util.encrypt(password);

        SysUser sysUser = SysUser.builder().username(param.getUsername()).realName(param.getRealName()).password(password).roleId(sysRole.getId()).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).build();

        if (RequestHolder.getCurrentUser() != null) { //SYSTEM_ADMIN
            sysUser.setCreator(RequestHolder.getCurrentUser().getUsername());
            sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }

        sysUser.setStatus(param.getStatus());
        sysUser.setRemark(param.getRemark());
        sysUser.setCreateTime(new Date());
        sysUser.setOperateTime(new Date());

        SysUser create = sysUserRepository.save(sysUser);
        if (RequestHolder.getCurrentUser() != null) { //SYSTEM_ADMIN
            SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_USER.getCode()).build();
            sysLogService.saveSysLog(null, create, sysLog);
        }
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户(id:" + id + ")不存在");
        sysUser.setStatus(stateCode.getCode());
        sysUserRepository.save(sysUser);
    }

    @Transactional
    public SysUser update(UserParam param) {
        BeanValidator.check(param);
        SysUser before = sysUserRepository.findOne(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户(id:" + param.getId() + ")不存在");
        if (!before.getUsername().equals(param.getUsername())) {
            throw new ParamException("用户名不能修改");
        }
        if (checkUsernameExist(param.getUsername(), param.getId())) {
            throw new ParamException("用户名已经存在");
        }
//        if (checkRealNameExist(param.getRealName(), param.getId())) {
//            throw new ParamException("用户真实姓名已经存在");
//        }
        if (checkTelExist(param.getTel(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        //查找角色
        SysRole sysRole = sysRoleService.findById(param.getId());

        SysUser after = SysUser.builder().username(param.getUsername()).realName(param.getRealName()).password(before.getPassword()).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).roleId(sysRole.getId()).build();
        after.setId(param.getId());

        after.setStatus(param.getStatus());
        after.setRemark(param.getRemark());
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        SysUser update = sysUserRepository.save(after);

        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_USER.getCode()).build();
        sysLogService.saveSysLog(before, after, sysLog);

        //更新用户，要更新session
        RequestHolder.getCurrentRequest().setAttribute("user", update);

        return update;
    }

    @Transactional
    public SysUser updatePassword(User user) {
        SysUser before = sysUserRepository.findOne(user.getId());
        Preconditions.checkNotNull(before, "待更改密码的用户(id:" + user.getId() + ")不存在");

        if (!before.getPassword().equals(MD5Util.encrypt(user.getOldPassword()))) {
            throw new ParamException("旧密码不正确");
        }
        before.setPassword(MD5Util.encrypt(user.getPassword()));

        SysUser update = sysUserRepository.save(before);

        //更新用户，要更新session
        RequestHolder.getCurrentRequest().setAttribute("user", update);

        return update;
    }

    public List<SysUser> findAll() {
        List<SysUser> sysUserList = (List<SysUser>) sysUserRepository.findAll();
        for (SysUser sysUser : sysUserList) {
            sysUser.setPassword(null);
            createUserAndRoleAndAcl(sysUser);
        }
        return sysUserList;
    }

    public SysUser findById(int id) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户(id:" + id + ")不存在");
        return sysUser;
    }

    public SysUser findByKeyword(String keyword) {
        Preconditions.checkNotNull(keyword, "查找用户的条件为空");
        return sysUserRepository.findByUsernameOrTelOrEmail(keyword, keyword, keyword);
    }

    private boolean checkUsernameExist(String username, Integer id) {
        if (id != null) {
            return sysUserRepository.countByUsernameAndIdNot(username, id) > 0;
        }
        return sysUserRepository.countByUsername(username) > 0;
    }

    private boolean checkRealNameExist(String realName, Integer id) {
        if (id != null) {
            return sysUserRepository.countByRealNameAndIdNot(realName, id) > 0;
        }
        return sysUserRepository.countByRealName(realName) > 0;
    }

    private boolean checkEmailExist(String email, Integer id) {
        if (!StringUtils.isBlank(email)) {
            if (id != null) {
                return sysUserRepository.countByEmailAndIdNot(email, id) > 0;
            }
            return sysUserRepository.countByEmail(email) > 0;
        }
        return false;
    }

    private boolean checkTelExist(String tel, Integer id) {
        if (!StringUtils.isBlank(tel)) {
            if (id != null) {
                return sysUserRepository.countByTelAndIdNot(tel, id) > 0;
            }
            return sysUserRepository.countByTel(tel) > 0;
        }
        return false;
    }

    public SysUser createUserAndRoleAndAcl(SysUser sysUser) {
        //给用户绑定角色
        SysRole sysRole = sysRoleService.findById(sysUser.getRoleId());
        sysUser.setSysRole(sysRole);

        //给用户绑定权限
        List<SysRoleAcl> sysRoleAclList = sysRoleAclRepository.findAllByRoleId(sysRole.getId());
        for (SysRoleAcl sysRoleAcl : sysRoleAclList) {
            SysAcl sysAcl = sysAclService.findById(sysRoleAcl.getAclId());
            sysUser.getSysAclList().add(sysAcl);
        }

        return sysUser;
    }
}
