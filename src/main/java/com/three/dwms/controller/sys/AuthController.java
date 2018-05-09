package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.param.sys.SessionUser;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.service.sys.SysUserService;
import com.three.dwms.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{props['init.remark']}")
    private String remark;

    @Resource
    private SysUserService sysUserService;

    @RequestMapping(value = "/noLogin")
    public JsonData unLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return JsonData.fail("没有登录，无法调用接口，请联系管理员");
    }

    @RequestMapping(value = "/noAuth")
    public JsonData unAccess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return JsonData.fail("没有访问权限，无法调用接口，请联系管理员");
    }

    @RequestMapping(value = "/initSuperAdmin", method = RequestMethod.GET)
    public JsonData initAdminData() {
        //超级管理员用户
        UserParam userParam = UserParam.builder().username(username).password(password).sex(1).status(stateCode).remark(remark).build();

        sysUserService.create(userParam);

        return JsonData.success();
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
                SessionUser sessionUser = sysUserService.bindSessionUser(sysUser);
                return JsonData.success(sessionUser);
            }
        }

        return JsonData.fail(errorMsg);
    }
}
