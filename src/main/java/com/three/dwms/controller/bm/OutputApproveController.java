package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;

import com.three.dwms.entity.bm.Output;
import com.three.dwms.param.bm.OutputApproveParam;
import com.three.dwms.service.bm.OutputApproveService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bm/outputApprove")
public class OutputApproveController {
    @Resource
    private OutputApproveService outputApproveService;

//    //申请通过
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData approve(@RequestBody List<OutputApproveParam> paramList) {
//        outputApproveService.approve(paramList);
//        return JsonData.success();
//    }
//
//    //审批拒绝
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData decline(@RequestBody List<OutputApproveParam> paramList) {
//        outputApproveService.decline(paramList);
//        return JsonData.success();
//    }
//
//    //取消审批通过或者拒绝状态
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData cancel(@RequestBody List<OutputApproveParam> paramList) {
//        outputApproveService.cancel(paramList);
//        return JsonData.success();
//    }
//
//    //加载个人审批单
//    @RequestMapping(method = RequestMethod.GET)
//    public JsonData load() {
//        List<Output> outputList = outputApproveService.load();
//        return JsonData.success(outputList);
//    }
}
