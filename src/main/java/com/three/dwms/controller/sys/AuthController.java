package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.param.sys.RoleUserAclParam;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.service.sys.SysRoleService;
import com.three.dwms.service.sys.SysUserService;
import com.three.dwms.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by csw on 2017/12/9.
 * Description:
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private Integer stateCode = StateCode.NORMAL.getCode();

    @Value("#{props['init.username']}")
    private String username;

    @Value("#{props['init.password']}")
    private String password;

    @Value("#{props['init.realName']}")
    private String realName;

    @Value("#{props['init.remark']}")
    private String remark;

    @Value("#{props['init.sessionInterval']}")
    private Integer sessionInterval;

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public JsonData initRoleData() {
        //初始化角色
        for (RoleTypeCode roleTypeCode : RoleTypeCode.values()) {
            RoleUserAclParam roleParam = RoleUserAclParam.builder().name(roleTypeCode.getName()).type(roleTypeCode.getType()).status(stateCode).remark(remark).build();
            sysRoleService.create(roleParam);
        }

        return JsonData.success();
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public JsonData initAdminData() {
        //系统管理员用户
        UserParam userParam = UserParam.builder().username(username).realName(realName).password(password).sex(1).roleId(1).status(stateCode).remark(remark).build();

        sysUserService.create(userParam);

        return JsonData.success();
    }

    @Resource
    private SysUserService sysUserService;

    @RequestMapping(value = "/noLogin")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonData unLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return JsonData.noLogin("没有登录，无法完成操作");
    }

    @RequestMapping(value = "/noAuth")
    public JsonData unAccess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return JsonData.fail("没有访问权限，无法完成操作");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public JsonData logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
        return JsonData.success();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonData login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = user.getUsername();
        String password = user.getPassword();

        SysUser sysUser;

        String errorMsg;

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else {
            sysUser = sysUserService.findByKeyword(username);
            if (sysUser == null) {
                errorMsg = "查询不到指定的用户";
            } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
                errorMsg = "密码错误";
            } else if (sysUser.getStatus() != 1) {
                errorMsg = "用户已被禁用，请联系管理员";
            } else {
                request.getSession().setAttribute("user", sysUser);
                request.getSession().setMaxInactiveInterval(sessionInterval);
                SysUser userRoleAcl = sysUserService.createUserAndRoleAndAcl(sysUser);
                return JsonData.success(userRoleAcl);
            }
        }

        return JsonData.fail(errorMsg);
    }
}
