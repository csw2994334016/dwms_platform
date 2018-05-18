package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Category;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.CategoryParam;
import com.three.dwms.param.basic.CategoryTree;
import com.three.dwms.repository.basic.CategoryRepository;
import com.three.dwms.repository.basic.ProductRepository;
import com.three.dwms.service.sys.SysLogService;
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
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private SysLogService sysLogService;

    @Transactional
    public void create(CategoryParam param) {
        BeanValidator.check(param);
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("分类名称已经存在");
        }

        Category category = Category.builder().name(param.getName()).build();

        category.setStatus(param.getStatus());
        category.setRemark(param.getRemark());
        category.setCreator(RequestHolder.getCurrentUser().getUsername());
        category.setCreateTime(new Date());
        category.setOperator(RequestHolder.getCurrentUser().getUsername());
        category.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        category.setOperateTime(new Date());

        categoryRepository.save(category);
    }

    private boolean checkNameExist(String categoryName, Integer id) {
        if (id != null) {
            return categoryRepository.countByNameAndIdNot(categoryName, id) > 0;
        }
        return categoryRepository.countByName(categoryName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Category category = this.findById(id);
        int count = productRepository.countByCategoryId(id);
        if (count > 0) {
            throw new ParamException("待删除物料分类信息中存在物料信息，请先修改物料信息的分类");
        }
        //假删除
        category.setStatus(stateCode.getCode());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Category> categories = Lists.newArrayList();
        for (Integer id : ids) {
            Category category = this.findById(id);
            int count = productRepository.countByCategoryId(id);
            if (count > 0) {
                throw new ParamException("待删除物料分类信息中存在物料信息，请先修改物料信息的分类");
            }
            categories.add(category);
        }
        categoryRepository.delete(categories);

//        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_CATEGORY.getCode()).build();
//        sysLogService.saveDeleteLog(ids, sysLog);
    }

    @Transactional
    public Category update(CategoryParam param) {
        Category category = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkNameExist(param.getName(), param.getId())) {
            throw new ParamException("分类名称已经存在");
        }

        category.setName(param.getName());
        category.setStatus(param.getStatus());
        category.setRemark(param.getRemark());
        category.setCreator(RequestHolder.getCurrentUser().getUsername());
        category.setCreateTime(new Date());
        category.setOperator(RequestHolder.getCurrentUser().getUsername());
        category.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        category.setOperateTime(new Date());

        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<CategoryTree> findAllByTree() {
        List<Category> categoryList = (List<Category>) categoryRepository.findAll();
        List<CategoryTree> categoryTreeList = Lists.newArrayList();
        CategoryTree categoryTree = CategoryTree.builder().text("物料分类").id(0).nodes(Lists.newArrayList()).build();
        for (Category category : categoryList) {
            CategoryTree categoryTree1 = CategoryTree.builder().id(category.getId()).text(category.getName()).build();
            categoryTree.getNodes().add(categoryTree1);
        }
        categoryTreeList.add(categoryTree);
        return categoryTreeList;
    }

    public Page<Category> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return categoryRepository.findAll(pageable);
    }

    public Category findById(int id) {
        Category category = categoryRepository.findOne(id);
        Preconditions.checkNotNull(category, "物料分类信息不存在");
        return category;
    }
}
