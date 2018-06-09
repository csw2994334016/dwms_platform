package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.entity.bm.BorrowDetail;
import com.three.dwms.param.bm.BorrowParam;
import com.three.dwms.param.bm.StateCode;
import com.three.dwms.service.bm.BorrowApplyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bm/borrowApplies")
public class BorrowApplyController {

    @Resource
    private BorrowApplyService borrowApplyService;

    //添加
    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody BorrowParam param) {
        borrowApplyService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable Integer id, @RequestBody BorrowParam param) {
        param.setId(id);
        borrowApplyService.update(param);
        return JsonData.success();
    }

    //查找所有申请单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Borrow> borrowList = borrowApplyService.findAll();
        return JsonData.success(borrowList);
    }

    //申请作废
    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData cancel(@RequestBody List<BorrowParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (BorrowParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        borrowApplyService.cancelByIds(ids);
        return JsonData.success();
    }

    //提交申请
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public JsonData submit(@RequestBody List<BorrowParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (BorrowParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        borrowApplyService.submitByIds(ids);
        return JsonData.success();
    }

    //撤回申请
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public JsonData withdraw(@RequestBody List<BorrowParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (BorrowParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        borrowApplyService.withdrawByIds(ids);
        return JsonData.success();
    }

    //OutputDetail详情
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public JsonData findBorrowDetails() {
        List<BorrowDetail> borrowDetailList = borrowApplyService.findBorrowDetails();
        return JsonData.success(borrowDetailList);
    }

    //加载个人申请单
    @RequestMapping(value = "/currentBorrowApplies", method = RequestMethod.GET)
    public JsonData findCurrentBorrowApplies() {
        List<Borrow> borrowList = borrowApplyService.findCurrentBorrowApplies();
        return JsonData.success(borrowList);
    }

    //加载借出状态
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public JsonData findStatus() {
        List<StateCode> stateCodeCodeList = borrowApplyService.findStatus();
        return JsonData.success(stateCodeCodeList);
    }
}
