package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.BanJi;
import com.three.dwms.param.basic.BanJiParam;
import com.three.dwms.service.basic.BanJiService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/banJis")
public class BanJiController {

    @Resource
    private BanJiService banJiService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody BanJiParam banJiParam) {
        banJiService.create(banJiParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        banJiService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<BanJiParam> banJiParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (BanJiParam banJiParam : banJiParamList) {
            if (banJiParam.getId() != null) {
                ids.add(banJiParam.getId());
            }
        }
        banJiService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody BanJiParam banJiParam) {
        banJiParam.setId(id);
        return JsonData.success(banJiService.update(banJiParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<BanJi> banJiList = banJiService.findAll();
        return JsonData.success(banJiList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<BanJi> categoryPage = banJiService.findAllByPage(pageQuery);
        return JsonData.success(categoryPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        BanJi banJi = banJiService.findById(id);
        return JsonData.success(banJi);
    }
}
