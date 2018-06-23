package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Record;
import com.three.dwms.param.bm.RecordParam;
import com.three.dwms.param.bm.StateCode;
import com.three.dwms.service.bm.RecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/6/23.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/records")
public class RecordController {

    @Resource
    private RecordService recordService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody RecordParam param) {
        recordService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<RecordParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (RecordParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        recordService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody RecordParam param) {
        param.setId(id);
        return JsonData.success(recordService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Record> projectList = recordService.findAll();
        return JsonData.success(projectList);
    }

    @RequestMapping(value = "/recordTypes", method = RequestMethod.GET)
    public JsonData findRecordType() {
        List<StateCode> stateCodeList = recordService.findRecordType();
        return JsonData.success(stateCodeList);
    }
}
