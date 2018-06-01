package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;

import com.three.dwms.entity.bm.Output;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.service.bm.OutputApproveService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bm/outputApproves")
public class OutputApproveController {

    @Resource
    private OutputApproveService outputApproveService;

    //加载个人审批单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Output> outputList = outputApproveService.findAll();
        return JsonData.success(outputList);
    }

    //申请通过
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public JsonData approve(@RequestBody List<OutputParam> paramList) {
        outputApproveService.approve(paramList);
        return JsonData.success();
    }

    //审批拒绝
    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    public JsonData decline(@RequestBody List<OutputParam> paramList) {
        outputApproveService.decline(paramList);
        return JsonData.success();
    }

    //取消审批通过或者拒绝状态
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public JsonData cancel(@RequestBody List<OutputParam> paramList) {
        outputApproveService.cancel(paramList);
        return JsonData.success();
    }

}
