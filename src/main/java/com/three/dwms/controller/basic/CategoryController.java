package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Category;
import com.three.dwms.param.basic.CategoryParam;
import com.three.dwms.service.basic.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody CategoryParam categoryParam) {
        categoryService.create(categoryParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        categoryService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<CategoryParam> categoryParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (CategoryParam categoryParam : categoryParamList) {
            if (categoryParam.getId() != null) {
                ids.add(categoryParam.getId());
            }
        }
        categoryService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody CategoryParam categoryParam) {
        categoryParam.setId(id);
        return JsonData.success(categoryService.update(categoryParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Category> categoryList = categoryService.findAll();
        return JsonData.success(categoryList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Category> categoryPage = categoryService.findAllByPage(pageQuery);
        return JsonData.success(categoryPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Category category = categoryService.findById(id);
        return JsonData.success(category);
    }
}
