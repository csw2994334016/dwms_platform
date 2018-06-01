package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Loc;
import com.three.dwms.param.basic.LocParam;
import com.three.dwms.service.basic.LocService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/locs")
public class LocController {

    @Resource
    private LocService locService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody LocParam param) {
        locService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        locService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<LocParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (LocParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        locService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody LocParam param) {
        param.setId(id);
        return JsonData.success(locService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Loc> locList = locService.findAll();
        return JsonData.success(locList);
    }

    @RequestMapping(value = "/warehouse", method = RequestMethod.POST)
    public JsonData findAllByWarehouse(@RequestBody LocParam param) {
        List<Loc> locList = locService.findAllByWarehouse(param);
        return JsonData.success(locList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Loc> page = locService.findAllByPage(pageQuery);
        return JsonData.success(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Loc loc = locService.findById(id);
        return JsonData.success(loc);
    }
}
