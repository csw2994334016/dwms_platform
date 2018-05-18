package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
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
        if (checkZoneCodeExist(param.getZoneCode(), param.getId())) {
            throw new ParamException("仓区编号已经存在");
        }
        if (checkZoneNameExist(param.getZoneName(), param.getId())) {
            throw new ParamException("仓区名称已经存在");
        }

        Warehouse warehouse = warehouseService.findById(param.getWarehouseId());

        Zone zone = Zone.builder().zoneCode(param.getZoneCode()).zoneName(param.getZoneName()).warehouse(warehouse).build();

        zone.setStatus(param.getStatus());
        zone.setRemark(param.getRemark());
        zone.setCreator(RequestHolder.getCurrentUser().getUsername());
        zone.setCreateTime(new Date());
        zone.setOperator(RequestHolder.getCurrentUser().getUsername());
        zone.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        zone.setOperateTime(new Date());

        zoneRepository.save(zone);
    }

    private boolean checkZoneCodeExist(String zoneCode, Integer id) {
        if (id != null) {
            return zoneRepository.countByZoneCodeAndIdNot(zoneCode, id) > 0;
        }
        return zoneRepository.countByZoneCode(zoneCode) > 0;
    }

    private boolean checkZoneNameExist(String zoneName, Integer id) {
        if (id != null) {
            return zoneRepository.countByZoneNameAndIdNot(zoneName, id) > 0;
        }
        return zoneRepository.countByZoneName(zoneName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Zone zone = this.findById(id);
        //假删除
        zone.setStatus(stateCode.getCode());
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
        BeanValidator.check(param);
        if (checkZoneCodeExist(param.getZoneCode(), param.getId())) {
            throw new ParamException("仓区编号已经存在");
        }
        if (checkZoneNameExist(param.getZoneName(), param.getId())) {
            throw new ParamException("仓区名称已经存在");
        }

        Warehouse warehouse = warehouseService.findById(param.getWarehouseId());

        zone.setZoneCode(param.getZoneCode());
        zone.setZoneName(param.getZoneName());
        zone.setWarehouse(warehouse);

        zone.setStatus(param.getStatus());
        zone.setRemark(param.getRemark());
        zone.setOperator(RequestHolder.getCurrentUser().getUsername());
        zone.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        zone.setOperateTime(new Date());

        return zoneRepository.save(zone);
    }

    public List<Zone> findAll() {
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
