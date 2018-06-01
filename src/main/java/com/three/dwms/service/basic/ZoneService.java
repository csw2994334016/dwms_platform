package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.basic.Zone;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.ZoneParam;
import com.three.dwms.repository.basic.ZoneRepository;
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
public class ZoneService {

    @Resource
    private ZoneRepository zoneRepository;

    @Resource
    private WarehouseService warehouseService;

    @Transactional
    public void create(ZoneParam param) {
        BeanValidator.check(param);
        Warehouse warehouse = warehouseService.findById(param.getPwhId());
        if (checkZoneCodeExist(param.getZoneCode(), warehouse, param.getId())) {
            throw new ParamException("仓区编号已经存在");
        }
        if (checkZoneNameExist(param.getZoneName(), warehouse, param.getId())) {
            throw new ParamException("仓区名称已经存在");
        }

        Zone zone = Zone.builder().zoneCode(param.getZoneCode()).zoneName(param.getZoneName()).warehouse(warehouse).build();

        zone.setStatus(param.getStatus());
        zone.setRemark(param.getZoneRemark());
        zone.setCreator(RequestHolder.getCurrentUser().getUsername());
        zone.setCreateTime(new Date());
        zone.setOperator(RequestHolder.getCurrentUser().getUsername());
        zone.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        zone.setOperateTime(new Date());

        zoneRepository.save(zone);
    }

    private boolean checkZoneCodeExist(String zoneCode, Warehouse warehouse, Integer id) {
        if (id != null) {
            if (warehouse != null) {
                return zoneRepository.countByZoneCodeAndWarehouseAndIdNot(zoneCode, warehouse, id) > 0;
            }
            return zoneRepository.countByZoneCodeAndIdNot(zoneCode, id) > 0;
        } else {
            if (warehouse != null) {
                return zoneRepository.countByZoneCodeAndWarehouse(zoneCode, warehouse) > 0;
            }
            return zoneRepository.countByZoneCode(zoneCode) > 0;
        }
    }

    private boolean checkZoneNameExist(String zoneName, Warehouse warehouse, Integer id) {
        if (id != null) {
            if (warehouse != null) {
                return zoneRepository.countByZoneNameAndWarehouseAndIdNot(zoneName, warehouse, id) > 0;
            }
            return zoneRepository.countByZoneNameAndIdNot(zoneName, id) > 0;
        } else {
            if (warehouse != null) {
                return zoneRepository.countByZoneNameAndWarehouse(zoneName, warehouse) > 0;
            }
            return zoneRepository.countByZoneName(zoneName) > 0;
        }
    }

    @Transactional
    public void updateStateById(int id, StatusCode statusCode) {
        Zone zone = this.findById(id);
        //假删除
        zone.setStatus(statusCode.getCode());
        zoneRepository.save(zone);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Zone> zones = Lists.newArrayList();
        for (Integer id : ids) {
            Zone zone = this.findById(id);
            zones.add(zone);
        }
        zoneRepository.delete(zones);

//        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_CATEGORY.getCode()).build();
//        sysLogService.saveDeleteLog(ids, sysLog);
    }

    @Transactional
    public Zone update(ZoneParam param) {
        Zone zone = this.findById(param.getId());
        Warehouse warehouse = warehouseService.findById(param.getPwhId());
        BeanValidator.check(param);
        if (checkZoneCodeExist(param.getZoneCode(), warehouse, param.getId())) {
            throw new ParamException("仓区编号已经存在");
        }
        if (checkZoneNameExist(param.getZoneName(), warehouse, param.getId())) {
            throw new ParamException("仓区名称已经存在");
        }

        zone.setZoneCode(param.getZoneCode());
        zone.setZoneName(param.getZoneName());
        zone.setWarehouse(warehouse);

        zone.setStatus(param.getStatus());
        zone.setRemark(param.getZoneRemark());
        zone.setOperator(RequestHolder.getCurrentUser().getUsername());
        zone.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        zone.setOperateTime(new Date());

        return zoneRepository.save(zone);
    }

    public List<Zone> findAll() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("pwhId") != null) {
            int pwhId = Integer.valueOf(request.getParameter("pwhId")); //表格查询条件
            if (pwhId > 0) {
                Warehouse warehouse = warehouseService.findById(pwhId);
                return zoneRepository.findAllByWarehouse(warehouse);
            } else {
                return (List<Zone>) zoneRepository.findAll();
            }
        }
        return (List<Zone>) zoneRepository.findAll();
    }

    public Page<Zone> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return zoneRepository.findAll(pageable);
    }

    public Zone findById(int id) {
        Zone zone = zoneRepository.findOne(id);
        Preconditions.checkNotNull(zone, "仓区信息不存在");
        return zone;
    }
}
