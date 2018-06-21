package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Borrow;
import com.three.dwms.param.bm.BorrowParam;
import com.three.dwms.service.bm.BorrowApproveService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bm/borrowApproves")
public class BorrowApproveController {

    @Resource
    private BorrowApproveService borrowApproveService;

    //加载个人审批单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Borrow> borrowList = borrowApproveService.findAll();
        return JsonData.success(borrowList);
    }

    //申请通过
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public JsonData approve(@RequestBody List<BorrowParam> paramList) {
        borrowApproveService.approve(paramList);
        return JsonData.success();
    }

    //审批拒绝
    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    public JsonData decline(@RequestBody List<BorrowParam> paramList) {
        borrowApproveService.decline(paramList);
        return JsonData.success();
    }

    //取消审批通过或者拒绝状态
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public JsonData cancel(@RequestBody List<BorrowParam> paramList) {
        borrowApproveService.cancel(paramList);
        return JsonData.success();
    }

}
