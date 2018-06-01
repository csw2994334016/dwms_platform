package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Zone;
import com.three.dwms.param.basic.ZoneParam;
import com.three.dwms.service.basic.ZoneService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/zones")
public class ZoneController {

    @Resource
    private ZoneService zoneService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody ZoneParam param) {
        zoneService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        zoneService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<ZoneParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (ZoneParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        zoneService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody ZoneParam param) {
        param.setId(id);
        return JsonData.success(zoneService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Zone> zoneList = zoneService.findAll();
        return JsonData.success(zoneList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Zone> page = zoneService.findAllByPage(pageQuery);
        return JsonData.success(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Zone zone = zoneService.findById(id);
        return JsonData.success(zone);
    }
}
