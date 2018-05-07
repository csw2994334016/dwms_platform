package com.three.dwms.controller.sys;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.service.sys.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/sys/users")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping(method = RequestMethod.GET)
    public JsonData getAll() {
        List<SysUser> sysUserList = sysUserService.getAll();
        return JsonData.success(sysUserList);
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create() {
        List<SysUser> sysUserList = sysUserService.getAll();
        return JsonData.success(sysUserList);
    }


}
