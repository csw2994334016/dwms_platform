package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.param.sys.UserRoleAcl;
import com.three.dwms.param.sys.User;
import com.three.dwms.param.sys.UserParam;
import com.three.dwms.service.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/sys/users")
@Slf4j
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody UserParam userParam) {
        sysUserService.create(userParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        sysUserService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody UserParam userParam) {
        userParam.setId(id);
        SysUser sysUser = sysUserService.update(userParam);
        SysUser userRoleAcl = sysUserService.createUserAndRoleAndAcl(sysUser);
        return JsonData.success(userRoleAcl);
    }

    @RequestMapping(value = "/password/{id}", method = RequestMethod.PUT)
    public JsonData updatePassword(@PathVariable int id, User user) {
        user.setId(id);
        SysUser sysUser = sysUserService.updatePassword(user);
        SysUser userRoleAcl = sysUserService.createUserAndRoleAndAcl(sysUser);
        return JsonData.success(sysUser);
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<SysUser> sysUserList = sysUserService.findAll();
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        SysUser sysUser = sysUserService.findById(id);
        SysUser userRoleAcl = sysUserService.createUserAndRoleAndAcl(sysUser);
        return JsonData.success(sysUser);
    }

}
