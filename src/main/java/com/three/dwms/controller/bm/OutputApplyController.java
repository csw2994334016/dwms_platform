package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Output;
import com.three.dwms.entity.bm.OutputDetail;
import com.three.dwms.param.bm.OutputParam;
import com.three.dwms.param.bm.StateCode;
import com.three.dwms.service.bm.OutputApplyService;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable Integer id, @RequestBody OutputParam param) {
        param.setId(id);
        outputApplyService.update(param);
        return JsonData.success();
    }

    //查找所有申请单
    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Output> outputList = outputApplyService.findAll();
        return JsonData.success(outputList);
    }

    //申请作废
    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData cancel(@RequestBody List<OutputParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (OutputParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        outputApplyService.cancelByIds(ids);
        return JsonData.success();
    }

    //提交申请
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public JsonData submit(@RequestBody List<OutputParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (OutputParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        outputApplyService.submitByIds(ids);
        return JsonData.success();
    }

    //撤回申请
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public JsonData withdraw(@RequestBody List<OutputParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (OutputParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        outputApplyService.withdrawByIds(ids);
        return JsonData.success();
    }

    //OutputDetail详情
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public JsonData findOutputDetails() {
        List<OutputDetail> outputDetailList = outputApplyService.findOutputDetails();
        return JsonData.success(outputDetailList);
    }

    //加载个人申请单
    @RequestMapping(value = "/currentOutputApplies", method = RequestMethod.GET)
    public JsonData findCurrentOutputApplies() {
        List<Output> outputList = outputApplyService.findCurrentOutputApplies();
        return JsonData.success(outputList);
    }

    //加载个人申请单
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public JsonData findStatus() {
        List<StateCode> stateCodeCodeList = outputApplyService.findStatus();
        return JsonData.success(stateCodeCodeList);
    }
}
