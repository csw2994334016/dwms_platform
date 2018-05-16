package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Project;
import com.three.dwms.entity.basic.Supplier;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.ProjectParam;
import com.three.dwms.param.basic.SupplierParam;
import com.three.dwms.repository.basic.SupplierRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@Service
public class SupplierService {

    @Resource
    private SupplierRepository supplierRepository;

    @Transactional
    public void create(SupplierParam param) {
        BeanValidator.check(param);
        if (checkSupplierCodeExist(param.getSupplierCode(), param.getId())) {
            throw new ParamException("供应商编号已经存在");
        }
        if (checkSupplierNameExist(param.getSupplierName(), param.getId())) {
            throw new ParamException("供应商名称已经存在");
        }

        Supplier supplier = Supplier.builder().supplierCode(param.getSupplierCode()).supplierName(param.getSupplierName()).build();

        supplier.setStatus(param.getStatus());
        supplier.setRemark(param.getRemark());
        supplier.setCreator(RequestHolder.getCurrentUser().getUsername());
        supplier.setCreateTime(new Date());
        supplier.setOperator(RequestHolder.getCurrentUser().getUsername());
        supplier.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        supplier.setOperateTime(new Date());

        supplierRepository.save(supplier);
    }

    private boolean checkSupplierCodeExist(String supplierCode, Integer id) {
        if (id != null) {
            return supplierRepository.countBySupplierCodeAndIdNot(supplierCode, id) > 0;
        }
        return supplierRepository.countBySupplierCode(supplierCode) > 0;
    }

    private boolean checkSupplierNameExist(String supplierName, Integer id) {
        if (id != null) {
            return supplierRepository.countBySupplierNameAndIdNot(supplierName, id) > 0;
        }
        return supplierRepository.countBySupplierName(supplierName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Supplier supplier = this.findById(id);
        //假删除
        supplier.setStatus(stateCode.getCode());
        supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Supplier> projects = Lists.newArrayList();
        for (Integer id : ids) {
            Supplier supplier = this.findById(id);
            projects.add(supplier);
        }
        supplierRepository.delete(projects);
    }

    public Supplier update(SupplierParam param) {
        Supplier supplier = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkSupplierCodeExist(param.getSupplierCode(), param.getId())) {
            throw new ParamException("供应商编号已经存在");
        }
        if (checkSupplierNameExist(param.getSupplierName(), param.getId())) {
            throw new ParamException("供应商名称已经存在");
        }

        supplier.setSupplierCode(param.getSupplierCode());
        supplier.setSupplierName(param.getSupplierName());

        supplier.setStatus(param.getStatus());
        supplier.setRemark(param.getRemark());
        supplier.setOperator(RequestHolder.getCurrentUser().getUsername());
        supplier.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        supplier.setOperateTime(new Date());

        return supplierRepository.save(supplier);
    }

    public List<Supplier> findAll() {
        return (List<Supplier>) supplierRepository.findAll();
    }

    public Page<Supplier> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return supplierRepository.findAll(pageable);
    }

    public Supplier findById(int id) {
        Supplier supplier = supplierRepository.findOne(id);
        Preconditions.checkNotNull(supplier, "供应商信息不存在");
        return supplier;
    }
}
