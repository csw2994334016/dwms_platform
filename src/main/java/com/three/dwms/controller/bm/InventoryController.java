package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.InventoryParam;
import com.three.dwms.service.bm.InventoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/inventories")
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @RequestMapping(value = "/whName", method = RequestMethod.POST)
    public JsonData findByWhName(@RequestBody InventoryParam param) {
        List<Inventory> inventoryList = inventoryService.findByWhName(param);
        return JsonData.success(inventoryList);
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Inventory> inventoryList = inventoryService.findAll();
        return JsonData.success(inventoryList);
    }
}
