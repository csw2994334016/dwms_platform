package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.AclTypeCode;
import com.three.dwms.constant.DefaultRole;
import com.three.dwms.constant.StateCode;
import com.three.dwms.param.sys.AclParam;
import com.three.dwms.param.sys.RoleParam;
import com.three.dwms.service.sys.SysAclService;
import com.three.dwms.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by csw on 2018/5/9.
 * Description: 初始化数据库默认数据接口
 */
@RestController
@RequestMapping(value = "/api/init")
public class InitDataController {

    private Integer stateCode = StateCode.NORMAL.getCode();

    @Value("#{props['init.username']}")
    private String username;

    @Value("#{props['init.password']}")
    private String password;

    @Value("#{props['init.remark']}")
    private String remark;

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public JsonData initRoleData() {
        //默认角色
        for (DefaultRole defaultRole : DefaultRole.values()) {
            RoleParam roleParam = RoleParam.builder().name(defaultRole.getName()).type(defaultRole.getType()).status(stateCode).remark(remark).build();
            sysRoleService.create(roleParam);
        }

        return JsonData.success();
    }
}
