package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.service.sys.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/6/19.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/queries")
public class QueryController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping(value = "/proposer", method = RequestMethod.GET)
    public JsonData findAllProposer() {
        List<SysUser> sysUserList = sysUserService.findAll();
        return JsonData.success(sysUserList);
    }

    @RequestMapping(value = "/approver", method = RequestMethod.GET)
    public JsonData findAllApprover() {
        List<SysUser> sysUserList = sysUserService.findAll();
        return JsonData.success(sysUserList);
    }
}
