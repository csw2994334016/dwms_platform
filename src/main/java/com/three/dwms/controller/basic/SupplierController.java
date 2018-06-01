package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Supplier;
import com.three.dwms.param.basic.SupplierParam;
import com.three.dwms.service.basic.SupplierService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/suppliers")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody SupplierParam param) {
        supplierService.create(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        supplierService.updateStateById(id, StatusCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<SupplierParam> paramList) {
        List<Integer> ids = Lists.newArrayList();
        for (SupplierParam param : paramList) {
            if (param.getId() != null) {
                ids.add(param.getId());
            }
        }
        supplierService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody SupplierParam param) {
        param.setId(id);
        return JsonData.success(supplierService.update(param));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Supplier> supplierList = supplierService.findAll();
        return JsonData.success(supplierList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Supplier> categoryPage = supplierService.findAllByPage(pageQuery);
        return JsonData.success(categoryPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Supplier supplier = supplierService.findById(id);
        return JsonData.success(supplier);
    }
}
