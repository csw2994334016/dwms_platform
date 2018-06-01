package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Area;
import com.three.dwms.param.basic.AreaParam;
import com.three.dwms.service.basic.AreaService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/areas")
public class AreaController {

    @Resource
    private AreaService areaService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody AreaParam param) {
        areaService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        areaService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<AreaParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (AreaParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        areaService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody AreaParam param) {
        param.setId(id);
        return JsonData.success(areaService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Area> zoneList = areaService.findAll();
        return JsonData.success(zoneList);
    }

    @RequestMapping(value = "/zone", method = RequestMethod.POST)
    public JsonData findAllByZone(@RequestBody AreaParam param) {
        List<Area> zoneList = areaService.findAllByZoneId(param.getPzoneId());
        return JsonData.success(zoneList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Area> page = areaService.findAllByPage(pageQuery);
        return JsonData.success(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Area area = areaService.findById(id);
        return JsonData.success(area);
    }
}
