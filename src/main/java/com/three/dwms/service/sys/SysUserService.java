package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.repository.sys.SysUserRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.MD5Util;
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
        String password = "888888";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder().username(param.getUsername()).password(encryptedPassword).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).build();

        sysUser.setStatus(StateCode.NORMAL.getCode());
        sysUser.setCreateId(RequestHolder.getCurrentUser().getId());
        sysUser.setCreateTime(new Date());

        sysUser.setOperatorId(RequestHolder.getCurrentUser().getId());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());

        sysUserRepository.save(sysUser);
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户不存在");
        sysUser.setStatus(stateCode.getCode());
        sysUserRepository.save(sysUser);
    }

    @Transactional
    public void update(UserParam param) {
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

        before.setUsername(param.getUsername());
        before.setEmail(param.getEmail());
        before.setTel(param.getTel());
        before.setSex(param.getSex());
        before.setStatus(param.getStatus());
        before.setRemark(param.getRemark());

        before.setOperatorId(RequestHolder.getCurrentUser().getId());
        before.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        before.setOperateTime(new Date());

        sysUserRepository.save(before);
    }

    public List<SysUser> findAll() {
        return (List<SysUser>) sysUserRepository.findAll();
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
}
