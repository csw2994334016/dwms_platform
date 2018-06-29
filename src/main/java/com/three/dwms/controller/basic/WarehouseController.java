package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.param.basic.WarehouseParam;
import com.three.dwms.param.basic.WarehouseTree;
import com.three.dwms.param.basic.VirtualZone;
import com.three.dwms.param.basic.WarehouseUses;
import com.three.dwms.service.basic.WarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/17.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/warehouses")
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody WarehouseParam param) {
        warehouseService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        warehouseService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<WarehouseParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (WarehouseParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        warehouseService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody WarehouseParam param) {
        param.setId(id);
        return JsonData.success(warehouseService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Warehouse> warehouseList = warehouseService.findAll();
        return JsonData.success(warehouseList);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public JsonData findAllByTree() {
        List<WarehouseTree> warehouseTreeList = warehouseService.findAllByTree();
        return JsonData.success(warehouseTreeList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Warehouse> page = warehouseService.findAllByPage(pageQuery);
        return JsonData.success(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Warehouse warehouse = warehouseService.findById(id);
        return JsonData.success(warehouse);
    }

    @RequestMapping(value = "/virtualWarehouses/{id}", method = RequestMethod.GET)
    public JsonData findVirtualWarehouse(@PathVariable Integer id) {
        List<VirtualZone> virtualZoneList = warehouseService.findVirtualWarehouse(id);
        return JsonData.success(virtualZoneList);
    }

    @RequestMapping(value = "/warehouseUses", method = RequestMethod.GET)
    public JsonData warehouseUses() {
        List<WarehouseUses> warehouseUsesList = warehouseService.findWarehouseUses();
        return JsonData.success(warehouseUsesList);
    }
}
