package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Zone;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.AreaParam;
import com.three.dwms.repository.basic.AreaRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AreaService {

    @Resource
    private ZoneService zoneService;

    @Resource
    private AreaRepository areaRepository;

    @Transactional
    public void create(AreaParam param) {
        BeanValidator.check(param);
        Zone zone = zoneService.findById(param.getPzoneId());
//        if (checkAreaNameExist(param.getAreaName(), zone, param.getId())) {
//            throw new ParamException("储区名称已经存在");
//        }

        //自动生成储区编号和名称
        Integer maxAreaCode = areaRepository.findMaxAreaCodeByZone(zone);
        int start = 0;
        if (maxAreaCode != null) {
            start = maxAreaCode;
        }
        List<Area> areaList = Lists.newArrayList();
        Area area;
        for (int i = start + 1; i <= start + param.getAreaNum(); i++) {
            area = Area.builder().areaCode(i).areaName(zone.getZoneCode() + "" + String.format("%02d", i)).zone(zone).build();
            area.setStatus(param.getStatus());
            area.setRemark(param.getRemark());
            area.setCreator(RequestHolder.getCurrentUser().getUsername());
            area.setCreateTime(new Date());
            area.setOperator(RequestHolder.getCurrentUser().getUsername());
            area.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            area.setOperateTime(new Date());
            areaList.add(area);
        }
        areaRepository.save(areaList);
    }

    private boolean checkAreaNameExist(String areaName, Zone zone, Integer id) {
        if (id != null) {
            if (zone != null) {
                return areaRepository.countByAreaNameAndZoneAndIdNot(areaName, zone, id) > 0;
            }
            return areaRepository.countByAreaNameAndIdNot(areaName, id) > 0;
        } else {
            if (zone != null) {
                return areaRepository.countByAreaNameAndZone(areaName, zone) > 0;
            }
            return areaRepository.countByAreaName(areaName) > 0;
        }
    }

    @Transactional
    public void updateStateById(int id, StatusCode statusCode) {
        Area area = this.findById(id);
        //假删除
        area.setStatus(statusCode.getCode());
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
        BeanValidator.check(param);
        Area area = this.findById(param.getId());
        Zone zone = zoneService.findById(param.getPzoneId());
        if (checkAreaNameExist(param.getAreaName(), zone, param.getId())) {
            throw new ParamException("储区名称已经存在");
        }

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
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("pzoneId") != null) {
            Zone zone = zoneService.findById(Integer.valueOf(request.getParameter("pzoneId")));
            return areaRepository.findAllByZone(zone);
        }
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

    public List<Area> findAllByZoneId(Integer zoneId) {
        if (zoneId != null) {
            Zone zone = zoneService.findById(zoneId);
            return areaRepository.findAllByZone(zone);
        }
        return Lists.newArrayList();
    }
}
