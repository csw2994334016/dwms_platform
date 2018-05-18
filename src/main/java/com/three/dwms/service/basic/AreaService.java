package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Zone;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.AreaParam;
import com.three.dwms.repository.basic.AreaRepository;
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
public class AreaService {

    @Resource
    private ZoneService zoneService;

    @Resource
    private AreaRepository areaRepository;

    @Transactional
    public void create(AreaParam param) {
        BeanValidator.check(param);
        if (checkAreaCodeExist(param.getAreaCode(), param.getId())) {
            throw new ParamException("储区编号已经存在");
        }
        if (checkAreaNameExist(param.getAreaName(), param.getId())) {
            throw new ParamException("储区名称已经存在");
        }

        Zone zone = zoneService.findById(param.getZoneId());

        Area area = Area.builder().areaCode(param.getAreaCode()).areaName(param.getAreaName()).zone(zone).build();

        area.setStatus(param.getStatus());
        area.setRemark(param.getRemark());
        area.setCreator(RequestHolder.getCurrentUser().getUsername());
        area.setCreateTime(new Date());
        area.setOperator(RequestHolder.getCurrentUser().getUsername());
        area.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        area.setOperateTime(new Date());

        areaRepository.save(area);
    }

    private boolean checkAreaCodeExist(Integer areaCode, Integer id) {
        if (id != null) {
            return areaRepository.countByAreaCodeAndIdNot(areaCode, id) > 0;
        }
        return areaRepository.countByAreaCode(areaCode) > 0;
    }

    private boolean checkAreaNameExist(String areaName, Integer id) {
        if (id != null) {
            return areaRepository.countByAreaNameAndIdNot(areaName, id) > 0;
        }
        return areaRepository.countByAreaName(areaName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StateCode stateCode) {
        Area area = this.findById(id);
        //假删除
        area.setStatus(stateCode.getCode());
        areaRepository.save(area);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Area> areas = Lists.newArrayList();
        for (Integer id : ids) {
            Area area = this.findById(id);
            areas.add(area);
        }
        areaRepository.delete(areas);

//        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_CATEGORY.getCode()).build();
//        sysLogService.saveDeleteLog(ids, sysLog);
    }

    @Transactional
    public Area update(AreaParam param) {
        Area area = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkAreaCodeExist(param.getAreaCode(), param.getId())) {
            throw new ParamException("储区编号已经存在");
        }
        if (checkAreaNameExist(param.getAreaName(), param.getId())) {
            throw new ParamException("储区名称已经存在");
        }

        Zone zone = zoneService.findById(param.getZoneId());

        area.setAreaCode(param.getAreaCode());
        area.setAreaName(param.getAreaName());
        area.setZone(zone);

        area.setStatus(param.getStatus());
        area.setRemark(param.getRemark());
        area.setOperator(RequestHolder.getCurrentUser().getUsername());
        area.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        area.setOperateTime(new Date());

        return areaRepository.save(area);
    }

    public List<Area> findAll() {
        return (List<Area>) areaRepository.findAll();
    }

    public Page<Area> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return areaRepository.findAll(pageable);
    }

    public Area findById(int id) {
        Area area = areaRepository.findOne(id);
        Preconditions.checkNotNull(area, "储区信息不存在");
        return area;
    }
}
