package com.three.dwms.controller.basic;

import com.google.common.collect.Lists;
import com.three.dwms.beans.JsonData;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Product;
import com.three.dwms.param.basic.ProductParam;
import com.three.dwms.service.basic.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by csw on 2018/5/13.
 * Description:
 */
@RestController
@RequestMapping(value = "/api/basic/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonData create(@RequestBody ProductParam productParam) {
        productService.create(productParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonData delete(@PathVariable int id) {
        productService.updateStateById(id, StateCode.DELETE);
        return JsonData.success();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.DELETE)
    public JsonData deleteBatch(@RequestBody List<ProductParam> productParamList) {
        List<Integer> ids = Lists.newArrayList();
        for (ProductParam productParam : productParamList) {
            if (productParam.getId() != null) {
                ids.add(productParam.getId());
            }
        }
        productService.deleteByIds(ids);
        return JsonData.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JsonData update(@PathVariable int id, @RequestBody ProductParam productParam) {
        productParam.setId(id);
        return JsonData.success(productService.update(productParam));
    }

    @RequestMapping(method = RequestMethod.GET)
    public JsonData findAll() {
        List<Product> productList = productService.findAll();
        return JsonData.success(productList);
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public JsonData findAll(@RequestBody PageQuery pageQuery) {
        Page<Product> productPage = productService.findAllByPage(pageQuery);
        return JsonData.success(productPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonData findOne(@PathVariable int id) {
        Product product = productService.findById(id);
        return JsonData.success(product);
    }
}
