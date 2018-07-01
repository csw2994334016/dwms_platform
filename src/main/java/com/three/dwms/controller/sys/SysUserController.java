package com.three.dwms.controller.sys;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.param.statics.StaticsParam;
import com.three.dwms.param.statics.Statics;
import com.three.dwms.param.sys.*;
import com.three.dwms.service.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        sysUserService.deleteById(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<UserParam> userParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (UserParam userParam : userParamList) {
            ids.add(userParam.getId());
        }
        sysUserService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody UserParam userParam) {
        userParam.setId(id);
        SysUser sysUser = sysUserService.update(userParam);
        return JsonData.success(sysUser);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public JsonData updateUser(@PathVariable int id, @RequestBody UserUpdateParam userParam) {
        userParam.setId(id);
        SysUser sysUser = sysUserService.updateUser(userParam);
        return JsonData.success(sysUser);
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public JsonData updatePassword(@RequestBody UserPasswordParam param) {
        sysUserService.updatePassword(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public JsonData resetPassword(@RequestBody UserParam param) {
        sysUserService.resetPassword(param);
        return JsonData.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<SysUser> sysUserList = sysUserService.findAll();
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/findByRoleType", method = RequestMethod.GET)
    public JsonData findAllByRoleType() {
        List<SysUser> sysUserList = sysUserService.findAllByRoleType();
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public JsonData findAllByRole(@PathVariable("id") int id) {
        List<SysUser> sysUserList = sysUserService.findAllByRole(id);
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<SysUser> sysUserList = sysUserService.findAllByPage(pageQuery);
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        SysUser sysUser = sysUserService.findById(id);
        return JsonData.success(sysUser);
    }

    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public JsonData findCurrentUser() {
        SysUser sysUser = sysUserService.findCurrentUser();
        return JsonData.success(sysUser);
    }

    @RequestMapping(value = "/currentMenu", method = RequestMethod.GET)
    public JsonData findCurrentMenu() {
        List<AclMenu> aclMenuList = sysUserService.findCurrentMenu();
        return JsonData.success(aclMenuList);
    }

    @RequestMapping(value = "/fuzzySearch", method = RequestMethod.POST)
    public JsonData fuzzySearch(@RequestBody UserParam userParam) {
        List<SysUser> sysUserList = sysUserService.fuzzySearch(userParam.getKeyword());
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/bindRole", method = RequestMethod.POST)
    public JsonData bindRole(@RequestBody UserRoleParam userRoleParam) {
        sysUserService.bindRole(userRoleParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/loginStatics", method = RequestMethod.GET)
    public JsonData loginStatics() {
        Statics statics = sysUserService.loginStatics();
        return JsonData.success(statics);
    }

}
