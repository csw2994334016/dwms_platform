package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Loc;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.LocParam;
import com.three.dwms.repository.basic.LocRepository;
import com.three.dwms.repository.basic.WarehouseRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
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
public class LocService {

    @Resource
    private AreaService areaService;

    @Resource
    private LocRepository locRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Transactional
    public void create(LocParam param) {
        BeanValidator.check(param);

        Area area = areaService.findById(param.getPareaId());

        //自动生成储位编号和名称
        Integer maxLocCode = locRepository.findMaxLocCodeByArea(area);
        int start = 0;
        if (maxLocCode != null) {
            start = maxLocCode;
        }
        List<Loc> locList = Lists.newArrayList();
        Loc loc;
        for (int i = start + 1; i <= start + param.getLocNum(); i++) {
            loc = Loc.builder().locCode(i).locName(area.getAreaName() + "" + String.format("%02d", i)).area(area).build();
            loc.setStatus(param.getStatus());
            loc.setRemark(param.getRemark());
            loc.setCreator(RequestHolder.getCurrentUser().getUsername());
            loc.setCreateTime(new Date());
            loc.setOperator(RequestHolder.getCurrentUser().getUsername());
            loc.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            loc.setOperateTime(new Date());
            locList.add(loc);
        }

        locRepository.save(locList);
    }

    private boolean checkLocCodeExist(Integer locCode, Integer id) {
        if (id != null) {
            return locRepository.countByLocCodeAndIdNot(locCode, id) > 0;
        }
        return locRepository.countByLocCode(locCode) > 0;
    }

    private boolean checkLocNameExist(String locName, Integer id) {
        if (id != null) {
            return locRepository.countByLocNameAndIdNot(locName, id) > 0;
        }
        return locRepository.countByLocName(locName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StatusCode statusCode) {
        Loc loc = this.findById(id);
        //假删除
        loc.setStatus(statusCode.getCode());
        locRepository.save(loc);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Loc> locs = Lists.newArrayList();
        for (Integer id : ids) {
            Loc loc = this.findById(id);
            locs.add(loc);
        }
        locRepository.delete(locs);

//        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_CATEGORY.getCode()).build();
//        sysLogService.saveDeleteLog(ids, sysLog);
    }

    @Transactional
    public Loc update(LocParam param) {
        Loc loc = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkLocCodeExist(param.getLocCode(), param.getId())) {
            throw new ParamException("储位编号已经存在");
        }
        if (checkLocNameExist(param.getLocName(), param.getId())) {
            throw new ParamException("储位名称已经存在");
        }
        Area area = areaService.findById(param.getPareaId());

        loc.setLocCode(param.getLocCode());
        loc.setLocName(param.getLocName());
        loc.setArea(area);

        loc.setStatus(param.getStatus());
        loc.setRemark(param.getRemark());
        loc.setOperator(RequestHolder.getCurrentUser().getUsername());
        loc.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        loc.setOperateTime(new Date());

        return locRepository.save(loc);
    }

    public List<Loc> findAll() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("pareaId") != null) {
            Area area = areaService.findById(Integer.valueOf(request.getParameter("pareaId")));
            return locRepository.findAllByArea(area);
        }
        return (List<Loc>) locRepository.findAll();
    }

    public List<Loc> findAllByWarehouse(LocParam param) {
        if (StringUtils.isBlank(param.getWhName())) {
            throw new ParamException("仓库编号或者名称不可以为空");
        }
        Warehouse warehouse = warehouseRepository.findByWhName(param.getWhName());
        return locRepository.findAllByWarehouse(warehouse);
    }

    public Page<Loc> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return locRepository.findAll(pageable);
    }

    public Loc findById(int id) {
        Loc loc = locRepository.findOne(id);
        Preconditions.checkNotNull(loc, "储位信息不存在");
        return loc;
    }

    public Loc findByLocNameAndWarehouse(String locName, Warehouse warehouse) {
        Loc loc = locRepository.findByLocNameAndWarehouse(locName, warehouse);
        Preconditions.checkNotNull(loc, "储位信息不存在");
        return loc;
    }
}
