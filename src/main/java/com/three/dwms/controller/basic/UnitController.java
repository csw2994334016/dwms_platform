package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Unit;
import com.three.dwms.param.basic.UnitParam;
import com.three.dwms.service.basic.UnitService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/units")
public class UnitController {

    @Resource
    private UnitService unitService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody UnitParam unitParam) {
        unitService.create(unitParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        unitService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<UnitParam> unitParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (UnitParam unitParam : unitParamList) {
            if (unitParam.getId() != null) {
                ids.add(unitParam.getId());
            }
        }
        unitService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody UnitParam unitParam) {
        unitParam.setId(id);
        return JsonData.success(unitService.update(unitParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Unit> categoryList = unitService.findAll();
        return JsonData.success(categoryList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Unit> categoryPage = unitService.findAllByPage(pageQuery);
        return JsonData.success(categoryPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Unit unit = unitService.findById(id);
        return JsonData.success(unit);
    }
}
