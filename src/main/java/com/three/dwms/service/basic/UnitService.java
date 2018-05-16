package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Unit;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.UnitParam;
import com.three.dwms.repository.basic.UnitRepository;
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
public class UnitService {

    @Resource
    private UnitRepository unitRepository;

    @Transactional
    public void create(UnitParam param) {
        BeanValidator.check(param);
        if (checkUnitCodeExist(param.getUnitCode(), param.getId())) {
            throw new ParamException("单位编号已经存在");
        }
        if (checkUnitNameExist(param.getUnitName(), param.getId())) {
            throw new ParamException("单位名称已经存在");
        }

        Unit unit = Unit.builder().unitCode(param.getUnitCode()).unitName(param.getUnitName()).build();

        unit.setStatus(param.getStatus());
        unit.setRemark(param.getRemark());
        unit.setCreator(RequestHolder.getCurrentUser().getUsername());
        unit.setCreateTime(new Date());
        unit.setOperator(RequestHolder.getCurrentUser().getUsername());
        unit.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        unit.setOperateTime(new Date());

        unitRepository.save(unit);
    }

    private boolean checkUnitCodeExist(String unitCode, Integer id) {
        if (id != null) {
            return unitRepository.countByUnitCodeAndIdNot(unitCode, id) > 0;
        }
        return unitRepository.countByUnitCode(unitCode) > 0;
    }

    private boolean checkUnitNameExist(String unitName, Integer id) {
        if (id != null) {
            return unitRepository.countByUnitNameAndIdNot(unitName, id) > 0;
        }
        return unitRepository.countByUnitName(unitName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Unit unit = this.findById(id);
        //假删除
        unit.setStatus(stateCode.getCode());
        unitRepository.save(unit);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Unit> units = Lists.newArrayList();
        for (Integer id : ids) {
            Unit unit = this.findById(id);
            units.add(unit);
        }
        unitRepository.delete(units);
    }

    public Unit update(UnitParam param) {
        Unit unit = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkUnitCodeExist(param.getUnitCode(), param.getId())) {
            throw new ParamException("单位编号已经存在");
        }
        if (checkUnitNameExist(param.getUnitName(), param.getId())) {
            throw new ParamException("单位名称已经存在");
        }

        unit.setUnitCode(param.getUnitCode());
        unit.setUnitName(param.getUnitName());

        unit.setStatus(param.getStatus());
        unit.setRemark(param.getRemark());
        unit.setOperator(RequestHolder.getCurrentUser().getUsername());
        unit.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        unit.setOperateTime(new Date());

        return unitRepository.save(unit);
    }

    public List<Unit> findAll() {
        return (List<Unit>) unitRepository.findAll();
    }

    public Page<Unit> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return unitRepository.findAll(pageable);
    }

    public Unit findById(int id) {
        Unit unit = unitRepository.findOne(id);
        Preconditions.checkNotNull(unit, "计量单位信息不存在");
        return unit;
    }
}
