package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.BorrowAllocationParam;
import com.three.dwms.service.bm.BorrowService;
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
@RequestMapping(value = "/api/bm/borrows")
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    //查找所有申请单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Borrow> borrowList = borrowService.findAll();
        return JsonData.success(borrowList);
    }

    //查找分配策略
    @RequestMapping(value = "/findInventory", method = RequestMethod.GET)
    public JsonData findInventory() {
        List<Inventory> inventoryList = borrowService.findInventory();
        return JsonData.success(inventoryList);
    }

    //拣货出库
    @RequestMapping(value = "/allocate", method = RequestMethod.POST)
    public JsonData allocate(@RequestBody List<BorrowAllocationParam> paramList) {
        borrowService.allocate(paramList);
        return JsonData.success();
    }

    //出库退还
    @RequestMapping(value = "/giveBack", method = RequestMethod.POST)
    public JsonData giveBack(@RequestBody List<BorrowAllocationParam> paramList) {
        borrowService.giveBack(paramList);
        return JsonData.success();
    }
}
