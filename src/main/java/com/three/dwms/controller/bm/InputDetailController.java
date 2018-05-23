package com.three.dwms.controller.bm;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.service.bm.InputDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/bm/inputDetails")
public class InputDetailController {

    @Resource
    private InputDetailService inputDetailService;

    @RequestMapping(value = "/upload")
    public JsonData batchImport(@RequestParam(value = "filename") MultipartFile file) {
        //判断文件是否为空
        if (file == null)
            return JsonData.fail();
        //获取文件名
        String filename = file.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (filename == null || ("").equals(filename) && size == 0)
            return JsonData.fail();

        boolean leadInOk = inputDetailService.batchImport(filename, file);

        return JsonData.success();
    }

    @RequestMapping(value = "/stockQuery", method = RequestMethod.GET)
    public JsonData stockQuery() {
        List<InputDetail> inputDetailList = inputDetailService.stockQuery();
        return JsonData.success(inputDetailList);
    }

}
