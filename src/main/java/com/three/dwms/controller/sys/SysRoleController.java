package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.service.sys.SysRoleService;
import com.three.dwms.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/sys/roles")
@Slf4j
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody RoleParam roleParam) {
        sysRoleService.create(roleParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        sysRoleService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody RoleParam roleParam) {
        roleParam.setId(id);
        return JsonData.success(sysRoleService.update(roleParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<SysRole> sysRoleList = sysRoleService.findAll();
        return JsonData.success(sysRoleList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        SysRole sysRole = sysRoleService.findById(id);
        return JsonData.success(sysRole);
    }

    @RequestMapping(value = "/bindUser", method = RequestMethod.PUT)
    public JsonData bindUser(@RequestParam int roleId, @RequestParam(required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToIntListByReg(userIds, ",");
        sysRoleService.bindUser(roleId, userIdList);
        return JsonData.success();
    }

    @RequestMapping(value = "/bindAcl", method = RequestMethod.PUT)
    public JsonData bindAcl(@RequestParam int roleId, @RequestParam(required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToIntListByReg(aclIds, ",");
        sysRoleService.bindAcl(roleId, aclIdList);
        return JsonData.success();
    }
}