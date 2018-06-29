package com.three.dwms.controller.sys;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.sys.SysNotice;
import com.three.dwms.param.sys.NoticeParam;
import com.three.dwms.service.sys.SysNoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/6/29.
 * Description:
 */
@RestController
@RequestMapping(value = "api/sys/notices")
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody NoticeParam param) {
        sysNoticeService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<NoticeParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (NoticeParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        sysNoticeService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody NoticeParam param) {
        param.setId(id);
        return JsonData.success(sysNoticeService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<SysNotice> sysNoticeList = sysNoticeService.findAll();
        return JsonData.success(sysNoticeList);
    }
}
