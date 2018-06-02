package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.param.bm.AllocationParam;
import com.three.dwms.service.bm.OutputService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/6/1.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/outputs")
public class OutputController {

    @Resource
    private OutputService outputService;

    //查找所有申请单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Output> outputList = outputService.findAll();
        return JsonData.success(outputList);
    }

    //查找分配策略
    @RequestMapping(value = "/findInventory", method = RequestMethod.GET)
    public JsonData findInventory() {
        List<Inventory> inventoryList = outputService.findInventory();
        return JsonData.success(inventoryList);
    }

    //拣货出库
    @RequestMapping(value = "/allocate", method = RequestMethod.POST)
    public JsonData allocate(@RequestBody List<AllocationParam> paramList) {
        outputService.allocate(paramList);
        return JsonData.success();
    }

    //出库退还
    @RequestMapping(value = "/giveBack", method = RequestMethod.POST)
    public JsonData giveBack(@RequestBody List<AllocationParam> paramList) {
        outputService.giveBack(paramList);
        return JsonData.success();
    }
}
