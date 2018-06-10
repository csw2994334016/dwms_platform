package com.three.dwms.service.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.AclTypeCode;
import com.three.dwms.constant.LogTypeCode;
import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.sys.*;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.sys.AclMenu;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.param.sys.UserRoleParam;
import com.three.dwms.repository.sys.SysRoleAclRepository;
import com.three.dwms.repository.sys.SysUserRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class SysUserService {

    @Value("#{props['init.defaultPassword']}")
    private String defaultPassword;

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private SysRoleService sysRoleService;

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

        String whCodes = StringUtils.join(param.getWhCodes(), ",");

        SysUser sysUser = SysUser.builder().username(param.getUsername()).realName(param.getRealName()).password(password).sysRole(sysRole).whCodes(whCodes).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).build();

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

        //更新用户-角色表

        if (RequestHolder.getCurrentUser() != null) { //SYSTEM_ADMIN
            SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_USER.getCode()).build();
            sysLogService.saveSysLog(null, create, sysLog);
        }
    }

    @Transactional
    public void updateStateById(int id, StatusCode statusCode) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户(id:" + id + ")不存在");
        sysUser.setStatus(statusCode.getCode());
        sysUserRepository.save(sysUser);
    }

    @Transactional
    public void deleteById(Integer id) {
        SysUser sysUser = this.findById(id);
        sysUserRepository.delete(sysUser);

        if (RequestHolder.getCurrentUser() != null) { //SYSTEM_ADMIN
            SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_USER.getCode()).build();
            sysLogService.saveSysLog(sysUser, null, sysLog);
        }
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<SysUser> sysUsers = Lists.newArrayList();
        for (Integer id : ids) {
            SysUser sysUser = this.findById(id);
            sysUsers.add(sysUser);
        }
        sysUserRepository.delete(sysUsers);

        if (RequestHolder.getCurrentUser() != null) { //SYSTEM_ADMIN
            for (SysUser sysUser : sysUsers) {
                SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_USER.getCode()).build();
                sysLogService.saveSysLog(sysUser, null, sysLog);
            }
        }
    }

    @Transactional
    public SysUser update(UserParam param) {
        BeanValidator.check(param);
        SysUser before = this.findById(param.getId());
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
        SysRole sysRole = sysRoleService.findById(param.getRoleId());
        String whCodes = StringUtils.join(param.getWhCodes(), ",");

        SysUser after = SysUser.builder().username(param.getUsername()).realName(param.getRealName()).password(before.getPassword()).tel(param.getTel()).email(param.getEmail()).sex(param.getSex()).sysRole(sysRole).whCodes(whCodes).build();
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
        update = bindUserWithAcl(update);
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
        }
        return sysUserList;
    }

    public Page<SysUser> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        Page<SysUser> sysUserPage = sysUserRepository.findAll(pageable);
        for (SysUser sysUser : sysUserPage.getContent()) {
            sysUser.setPassword(null);
        }
        return sysUserPage;
    }

    public List<SysUser> findAllByRole(Integer id) {
        SysRole sysRole = sysRoleService.findById(id);
        List<SysUser> sysUserList = sysUserRepository.findAllBySysRole(sysRole);
        for (SysUser sysUser : sysUserList) {
            sysUser.setPassword(null);
        }
        return sysUserList;
    }

    public SysUser findById(int id) {
        SysUser sysUser = sysUserRepository.findOne(id);
        Preconditions.checkNotNull(sysUser, "用户(id:" + id + ")不存在");
        return sysUser;
    }

    public List<SysUser> findAllByRoleType() {
        List<SysUser> sysUserList = Lists.newArrayList();
        List<SysUser> sysUserList1 = Lists.newArrayList();
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser != null && RoleTypeCode.ADMIN.getType().equals(sysUser.getSysRole().getType())) {
            sysUserList1 = (List<SysUser>) sysUserRepository.findAll();
        } else {
            sysUserList1.add(sysUser);
        }
        for (SysUser sysUser1 : sysUserList1) {
            SysUser sysUser2 = SysUser.builder().username(sysUser1.getUsername()).build();
            sysUserList.add(sysUser2);
        }
        return sysUserList;
    }

    public SysUser findCurrentUser() {
        Integer id = RequestHolder.getCurrentUser().getId();
        return this.findById(id);
    }

    public List<AclMenu> findCurrentMenu() {
        List<AclMenu> aclMenuList = Lists.newArrayList();
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (RoleTypeCode.ADMIN.getType().equals(sysUser.getSysRole().getType())) {
            List<SysAcl> sysAclList = sysAclService.findAll();
            for (SysAcl sysAcl : sysAclList) {
                if (AclTypeCode.TYPE_MENU.getCode() == sysAcl.getType()) {
                    AclMenu aclMenu = AclMenu.builder().id(sysAcl.getId().toString()).parentId(sysAcl.getParentId().toString()).name(sysAcl.getName()).icon(sysAcl.getIcon()).url(sysAcl.getUrl()).build();
                    aclMenuList.add(aclMenu);
                }
            }
        } else {
            List<SysRoleAcl> sysRoleAclList = sysRoleAclRepository.findAllByRoleId(sysUser.getSysRole().getId());
            for (SysRoleAcl sysRoleAcl : sysRoleAclList) {
                SysAcl sysAcl = sysAclService.findById(sysRoleAcl.getAclId());
                if (AclTypeCode.TYPE_MENU.getCode() == sysAcl.getType()) {
                    AclMenu aclMenu = AclMenu.builder().id(sysAcl.getId().toString()).parentId(sysAcl.getParentId().toString()).name(sysAcl.getName()).icon(sysAcl.getIcon()).url(sysAcl.getUrl()).build();
                    aclMenuList.add(aclMenu);
                }
            }
        }
        return aclMenuList;
    }

    public SysUser findByKeyword(String keyword) {
        Preconditions.checkNotNull(keyword, "查找用户的关键字不可以为空");
        return sysUserRepository.findByUsernameOrTelOrEmail(keyword, keyword, keyword);
    }

    public List<SysUser> fuzzySearch(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return this.findAll();
        } else {
            List<SysUser> sysUserList = sysUserRepository.findAllByUsernameContainingOrTelContainingOrEmailContaining(keyword, keyword, keyword);
            for (SysUser sysUser : sysUserList) {
                sysUser.setPassword(null);
            }
            return sysUserList;
        }
    }

    public void bindRole(UserRoleParam userRoleParam) {
        BeanValidator.check(userRoleParam);
        SysUser sysUser = this.findById(userRoleParam.getUserId());
        SysRole sysRole = sysRoleService.findById(userRoleParam.getRoleId());
        sysUser.setSysRole(sysRole);
        sysUserRepository.save(sysUser);
        //更新用户-角色表
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

    public SysUser bindUserWithAcl(SysUser sysUser) {

        //给用户绑定权限
        List<SysRoleAcl> sysRoleAclList = sysRoleAclRepository.findAllByRoleId(sysUser.getSysRole().getId());
        for (SysRoleAcl sysRoleAcl : sysRoleAclList) {
            sysUser.getAlcIdList().add(sysRoleAcl.getAclId());
        }

        return sysUser;
    }
}
