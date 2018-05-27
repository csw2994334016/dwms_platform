package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.service.bm.OutputApplyService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bm/outputApplies")
public class OutputApplyController {

    @Resource
    private OutputApplyService outputApplyService;

    //添加
    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody OutputParam param) {
        outputApplyService.create(param);
        return JsonData.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Output> outputList = outputApplyService.findAll();
        return JsonData.success(outputList);
    }

//    //更新
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData update(@RequestBody List<OutputParam> paramList) {
//        outputApplyService.update(paramList);
//        return JsonData.success();
//    }
//
//    //作废
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData cancel(@RequestBody List<OutputParam> paramList) {
//        outputApplyService.cancle(paramList);
//        return JsonData.success();
//    }
//
//    //提交申请
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData submit(@RequestBody List<OutputParam> paramList) {
//        outputApplyService.submit(paramList);
//        return JsonData.success();
//    }
//
//    //OutputDetail详情
//    @RequestMapping(method = RequestMethod.PUT)
//    public JsonData findAll(@RequestBody Integer id) {
//        OutputDetail outputDetail = outputApplyService.findAll(id);
//        return JsonData.success(outputDetail);
//    }
//
//    //加载个人申请单
//    @RequestMapping(method = RequestMethod.GET)
//    public JsonData load() {
//        List<Output> outputList = outputApplyService.load();
//        return JsonData.success(outputList);
//    }
}
