package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.param.bm.InputDetailParam;
import com.three.dwms.param.statics.StaticsParam;
import com.three.dwms.param.statics.Statics;
import com.three.dwms.service.bm.InputDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/inputDetails")
@Slf4j
public class InputDetailController {

    @Resource
    private InputDetailService inputDetailService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonData batchImport(@RequestParam(value = "filename") MultipartFile file) {
        //判断文件是否为空
        if (file == null)
            return JsonData.fail("上传的文件为空");
        //获取文件名
        String filename = file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (filename == null || ("").equals(filename) && size == 0)
            return JsonData.fail("上传的文件为空");

        List<InputDetail> inputDetailList = inputDetailService.batchImport(filename, file);
        log.info("上传文件信息，文件名：{}，解析记录数：{}", filename, inputDetailList.size());
        return JsonData.success(inputDetailList);
    }

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody List<InputDetailParam> paramList) {
        inputDetailService.create(paramList);
        return JsonData.success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<InputDetail> inputDetailList = inputDetailService.findAll();
        return JsonData.success(inputDetailList);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<InputDetailParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (InputDetailParam projectParam : paramList) {
            if (projectParam.getId() != null) {
                ids.add(projectParam.getId());
            }
        }
        inputDetailService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/stockQuery", method = RequestMethod.GET)
    public JsonData stockQuery() {
        List<InputDetail> inputDetailList = inputDetailService.stockQuery();
        return JsonData.success(inputDetailList);
    }

    @RequestMapping(value = "/inputStatics", method = RequestMethod.POST)
    public JsonData inputStatics(@RequestBody StaticsParam param) {
        Statics statics = inputDetailService.inputStatics(param);
        return JsonData.success(statics);
    }

}
