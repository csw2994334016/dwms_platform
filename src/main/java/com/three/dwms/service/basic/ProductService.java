package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Category;
import com.three.dwms.entity.basic.Product;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.ProductParam;
import com.three.dwms.repository.basic.ProductRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CategoryService categoryService;

    @Transactional
    public void create(ProductParam param) {
        BeanValidator.check(param);
        if (checkSkuExist(param.getSku(), param.getId())) {
            throw new ParamException("物料编号已经存在");
        }
        if (checkSkuDescExist(param.getSkuDesc(), param.getId())) {
            throw new ParamException("物料名称已经存在");
        }

        Category category = categoryService.findById(param.getCategoryId());

        Product product = Product.builder().sku(param.getSku()).skuDesc(param.getSkuDesc()).category(category).safeNumber(param.getSafeNumber()).build();

        product.setStatus(param.getStatus());
        product.setRemark(param.getRemark());
        product.setCreator(RequestHolder.getCurrentUser().getUsername());
        product.setCreateTime(new Date());
        product.setOperator(RequestHolder.getCurrentUser().getUsername());
        product.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        product.setOperateTime(new Date());

        productRepository.save(product);
    }

    private boolean checkSkuDescExist(String skuDesc, Integer id) {
        if (id != null) {
            return productRepository.countBySkuDescAndIdNot(skuDesc, id) > 0;
        }
        return productRepository.countBySkuDesc(skuDesc) > 0;
    }

    private boolean checkSkuExist(String sku, Integer id) {
        if (id != null) {
            return productRepository.countBySkuAndIdNot(sku, id) > 0;
        }
        return productRepository.countBySku(sku) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode delete) {
        Product product = this.findById(id);
        product.setStatus(delete.getCode());
        productRepository.save(product);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Product> products = Lists.newArrayList();
        for (Integer id : ids) {
            products.add(this.findById(id));
        }
        productRepository.delete(products);
    }

    @Transactional
    public Product update(ProductParam param) {
        Product product = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkSkuExist(param.getSku(), param.getId())) {
            throw new ParamException("物料编号已经存在");
        }
        if (checkSkuDescExist(param.getSkuDesc(), param.getId())) {
            throw new ParamException("物料名称已经存在");
        }

        Category category = categoryService.findById(param.getCategoryId());

        product.setSku(param.getSku());
        product.setSkuDesc(param.getSkuDesc());
        product.setSpec(param.getSpec());
        product.setCategory(category);
        product.setSafeNumber(param.getSafeNumber());

        product.setStatus(param.getStatus());
        product.setRemark(param.getRemark());
        product.setOperator(RequestHolder.getCurrentUser().getUsername());
        product.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        product.setOperateTime(new Date());

        return productRepository.save(product);
    }

    public List<Product> findAll() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("categoryId") != null) {
            int categoryId = Integer.valueOf(request.getParameter("categoryId")); //表格查询条件
            if (categoryId > 0) {
                Category category = categoryService.findById(categoryId);
                return productRepository.findAllByCategory(category);
            } else {
                return (List<Product>) productRepository.findAll();
            }
        }
        return (List<Product>) productRepository.findAll();
    }

    public Page<Product> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return productRepository.findAll(pageable);
    }

    public Product findById(int id) {
        Product product = productRepository.findOne(id);
        Preconditions.checkNotNull(product, "物料信息不存在");
        return product;
    }

}
