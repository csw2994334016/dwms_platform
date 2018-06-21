package com.three.dwms.controller.bm;

import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.Check;
import com.three.dwms.entity.bm.CheckDetail;
import com.three.dwms.param.bm.CheckParam;
import com.three.dwms.service.bm.CheckService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/checks")
public class CheckController {

    @Resource
    private CheckService checkService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody CheckParam param) {
        checkService.create(param);
        return JsonData.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Check> checkList = checkService.findAll();
        return JsonData.success(checkList);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public JsonData findCheckDetail() {
        List<CheckDetail> checkDetailList = checkService.findCheckDetail();
        return JsonData.success(checkDetailList);
    }
}
