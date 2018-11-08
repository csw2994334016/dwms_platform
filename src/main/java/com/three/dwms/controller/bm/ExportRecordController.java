package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.constant.ResultCode;
import com.three.dwms.entity.bm.ExportRecord;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.param.bm.ExportRecordParam;
import com.three.dwms.service.bm.ExportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/exportRecords")
public class ExportRecordController {

    @Autowired
    private ExportRecordService exportRecordService;

    @RequestMapping(value = "/findInputDetails", method = RequestMethod.GET)
    public JsonData findInputDetails() {
        List<InputDetail> inputDetailList = exportRecordService.findInputDetails();
        return JsonData.success(inputDetailList);
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<ExportRecord> exportRecordList = exportRecordService.findAll();
        return JsonData.success(exportRecordList);
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public JsonData exportRecord(@RequestBody List<ExportRecordParam> paramList) {
        String fileName = exportRecordService.exportRecord(paramList);
        return JsonData.success(fileName, ResultCode.SUCCESS.getDesc());
    }
}
