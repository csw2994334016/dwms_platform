package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.SessionUser;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.repository.sys.SysUserRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.MD5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@Service
public class SysUserService {

    @Value("#{props['init.username']}")
    private String username;

    @Resource
    private SysUserRepository sysUserRepository;

    @Transactional
    public void create(UserParam param) {
        BeanValidator.check(param);
        if (checkUsernameExist(param.getUsername(), param.getId())) {
            throw new ParamException("用户名已经存在");
        }
        if (checkTelephoneExist(param.getTel(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        String encryptedPassword = MD5Util.encrypt(param.getPassword());
        SysUser sysUser = SysUser.builder().username(param.getUsername()).password(encryptedPassword).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).build();

        SysUser curUser = RequestHolder.getCurrentUser();
        if (curUser != null) {
            sysUser.setCreator(RequestHolder.getCurrentUser().getUsername());
            sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        }

        sysUser.setStatus(param.getStatus());
        sysUser.setRemark(param.getRemark());
        sysUser.setCreateTime(new Date());
        sysUser.setOperateTime(new Date());

        sysUserRepository.save(sysUser);

        //添加用户-角色
//        int count = sysRoleRepository.countByCode(roleCode);
//        if (count == 0) {
//            throw new ParamException("添加用户时，根据角色代码没有查找到相应角色");
//        } else if (count == 1) {
//            SysRole sysRole = sysRoleRepository.findByCode(roleCode);
//            SysUser createUser = sysUserRepository.findByUsername(param.getUsername());
//            SysUserRole sysUserRole = SysUserRole.builder().userId(createUser.getId()).roleId(sysRole.getId()).build();
//            sysUserRoleRepository.save(sysUserRole);
//        } else {
//            throw new ParamException("角色代码异常，数据库有相同的角色代码");
//        }
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户不存在");
        sysUser.setStatus(stateCode.getCode());
        sysUserRepository.save(sysUser);
    }

    @Transactional
    public SysUser update(UserParam param) {
        BeanValidator.check(param);
        if (checkUsernameExist(param.getUsername(), param.getId())) {
            throw new ParamException("用户名已经存在");
        }
        if (checkTelephoneExist(param.getTel(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkEmailExist(param.getEmail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        SysUser before = sysUserRepository.findOne(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        if (!before.getUsername().equals(param.getUsername())) {
            throw new ParamException("用户名不能修改");
        }

        before.setEmail(param.getEmail());
        before.setTel(param.getTel());
        before.setSex(param.getSex());
        before.setStatus(param.getStatus());
        before.setRemark(param.getRemark());

        before.setOperator(RequestHolder.getCurrentUser().getUsername());
        before.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        before.setOperateTime(new Date());

        SysUser update = sysUserRepository.save(before);

        //更新用户，要更新session
        RequestHolder.getCurrentRequest().setAttribute("user", update);

        return update;
    }

    @Transactional
    public SysUser updatePassword(User user) {
        SysUser before = sysUserRepository.findOne(user.getId());
        Preconditions.checkNotNull(before, "待更改密码的用户不存在");

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
        }
        return sysUserList;
    }

    public SysUser findByKeyword(String keyword) {
        Preconditions.checkNotNull(keyword, "查找用户条件为空");
        return sysUserRepository.findByUsernameOrTelOrEmail(keyword, keyword, keyword);
    }

    private boolean checkUsernameExist(String username, Integer id) {
        if (id != null) {
            return sysUserRepository.countByUsernameAndId(username, id) > 0;
        }
        return sysUserRepository.countByUsername(username) > 0;
    }

    private boolean checkEmailExist(String email, Integer id) {
        if (id != null) {
            return sysUserRepository.countByEmailAndId(email, id) > 0;
        }
        return sysUserRepository.countByEmail(email) > 0;
    }

    private boolean checkTelephoneExist(String tel, Integer id) {
        if (id != null) {
            return sysUserRepository.countByTelAndId(tel, id) > 0;
        }
        return sysUserRepository.countByTel(tel) > 0;
    }

    public SessionUser bindSessionUser(SysUser sysUser) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setSysUser(sysUser);
        return sessionUser;
    }
}
