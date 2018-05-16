package com.three.dwms.controller.sys;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.param.sys.AclParam;
import com.three.dwms.param.sys.AclTree;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.service.sys.SysAclService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/sys/acls")
public class SysAclController {

    @Resource
    private SysAclService sysAclService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody AclParam aclParam) {
        sysAclService.create(aclParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        sysAclService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<AclParam> aclParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (AclParam aclParam : aclParamList) {
            if (aclParam.getId() != null) {
                ids.add(aclParam.getId());
            }
        }
        sysAclService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody AclParam aclParam) {
        aclParam.setId(id);
        return JsonData.success(sysAclService.update(aclParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<SysAcl> sysAclList = sysAclService.findAll();
        return JsonData.success(sysAclList);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public JsonData findAllByRole(@PathVariable("id") int id) {
        List<SysAcl> sysAclList = sysAclService.findAllByRole(id);
        return JsonData.success(sysAclList);
    }

    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    public JsonData findAllByRoles(@PathVariable("id") int id) {
        List<Integer> aclIdLIst = sysAclService.findAllByRoles(id);
        return JsonData.success(aclIdLIst);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public JsonData findAllByTree() {
        List<AclTree> aclTreeList = sysAclService.findAllByTree();
        return JsonData.success(aclTreeList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        SysAcl sysAcl = sysAclService.findById(id);
        return JsonData.success(sysAcl);
    }
}
