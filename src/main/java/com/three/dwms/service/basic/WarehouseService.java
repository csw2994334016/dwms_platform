package com.three.dwms.service.basic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.beans.PageQuery;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.constant.WhTypeCode;
import com.three.dwms.entity.basic.Area;
import com.three.dwms.entity.basic.Loc;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.basic.Zone;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.basic.*;
import com.three.dwms.param.bm.InventoryParam;
import com.three.dwms.repository.basic.AreaRepository;
import com.three.dwms.repository.basic.LocRepository;
import com.three.dwms.repository.basic.WarehouseRepository;
import com.three.dwms.repository.basic.ZoneRepository;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.service.bm.InventoryService;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@Service
public class WarehouseService {

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private ZoneRepository zoneRepository;

    @Resource
    private AreaRepository areaRepository;

    @Resource
    private LocRepository locRepository;

    @Resource
    private InventoryRepository inventoryRepository;

    @Transactional
    public void create(WarehouseParam param) {
        BeanValidator.check(param);
        long whCount = warehouseRepository.count();
        if (whCount >= 2) {
            throw new ParamException("系统最多支持创建量仓库");
        }
        if (checkWhCodeExist(param.getWhCode(), param.getId())) {
            throw new ParamException("仓库编号已经存在");
        }
        if (checkWhNameExist(param.getWhName(), param.getId())) {
            throw new ParamException("仓库名称已经存在");
        }

        Warehouse warehouse = Warehouse.builder().whCode(param.getWhCode()).whName(param.getWhName()).build();

        warehouse.setStatus(param.getStatus());
        warehouse.setRemark(param.getWhRemark());
        warehouse.setCreator(RequestHolder.getCurrentUser().getUsername());
        warehouse.setCreateTime(new Date());
        warehouse.setOperator(RequestHolder.getCurrentUser().getUsername());
        warehouse.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        warehouse.setOperateTime(new Date());

        warehouseRepository.save(warehouse);
    }

    private boolean checkWhCodeExist(String whCode, Integer id) {
        if (id != null) {
            return warehouseRepository.countByWhCodeAndIdNot(whCode, id) > 0;
        }
        return warehouseRepository.countByWhCode(whCode) > 0;
    }

    private boolean checkWhNameExist(String whName, Integer id) {
        if (id != null) {
            return warehouseRepository.countByWhNameAndIdNot(whName, id) > 0;
        }
        return warehouseRepository.countByWhName(whName) > 0;
    }

    @Transactional
    public void updateStateById(int id, StatusCode statusCode) {
        Warehouse warehouse = this.findById(id);
        //假删除
        warehouse.setStatus(statusCode.getCode());
        warehouseRepository.save(warehouse);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Warehouse> warehouses = Lists.newArrayList();
        for (Integer id : ids) {
            Warehouse warehouse = this.findById(id);
            warehouses.add(warehouse);
        }
        warehouseRepository.delete(warehouses);

//        SysLog sysLog = SysLog.builder().type(LogTypeCode.TYPE_CATEGORY.getCode()).build();
//        sysLogService.saveDeleteLog(ids, sysLog);
    }

    @Transactional
    public Warehouse update(WarehouseParam param) {
        Warehouse warehouse = this.findById(param.getId());
        BeanValidator.check(param);
        if (checkWhCodeExist(param.getWhCode(), param.getId())) {
            throw new ParamException("仓库编号已经存在");
        }
        if (checkWhNameExist(param.getWhName(), param.getId())) {
            throw new ParamException("仓库名称已经存在");
        }

        warehouse.setWhCode(param.getWhCode());
        warehouse.setWhName(param.getWhName());

        warehouse.setStatus(param.getStatus());
        warehouse.setRemark(param.getWhRemark());
        warehouse.setOperator(RequestHolder.getCurrentUser().getUsername());
        warehouse.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        warehouse.setOperateTime(new Date());

        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        List<Warehouse> warehouseList;
        String whCodes = RequestHolder.getCurrentUser().getWhCodes();
        if (StringUtils.isBlank(whCodes)) {
            throw new ParamException("用户没有访问仓库的权限");
        }
        List<String> whCodeList = Arrays.asList(StringUtils.split(whCodes, ","));
        warehouseList = warehouseRepository.findAllByWhCodeIn(whCodeList);
        return warehouseList;
    }

    public List<WarehouseTree> findAllByTree() {
        List<WarehouseTree> warehouseTreeList = Lists.newArrayList();
        String whCodes = RequestHolder.getCurrentUser().getWhCodes();
        List<String> whCodeList = Arrays.asList(StringUtils.split(whCodes, ","));
        List<Warehouse> warehouseList = warehouseRepository.findAllByWhCodeIn(whCodeList);
        WarehouseTree root = WarehouseTree.builder().id(0).text(WhTypeCode.ROOT.getDesc()).type(WhTypeCode.ROOT.getCode()).nodes(Lists.newArrayList()).build();
        for (Warehouse warehouse : warehouseList) {
            WarehouseTree warehouseTree1 = WarehouseTree.builder().id(warehouse.getId()).text(warehouse.getWhName()).type(WhTypeCode.WAREHOUSE.getCode()).build();
            List<Zone> zoneList = zoneRepository.findAllByWarehouseOrderByZoneCodeAsc(warehouse);
            if (zoneList.size() > 0) {
                warehouseTree1.setNodes(Lists.newArrayList());
            }
            for (Zone zone : zoneList) {
                WarehouseTree warehouseTree2 = WarehouseTree.builder().id(zone.getId()).text(zone.getZoneName()).type(WhTypeCode.ZONE.getCode()).build();
                List<Area> areaList = areaRepository.findAllByZoneOrderByAreaNameAsc(zone);
                if (areaList.size() > 0) {
                    warehouseTree2.setNodes(Lists.newArrayList());
                }
                for (Area area : areaList) {
                    WarehouseTree warehouseTree3 = WarehouseTree.builder().id(area.getId()).text(area.getAreaName()).type(WhTypeCode.AREA.getCode()).build();
                    List<Loc> locList = locRepository.findAllByAreaOrderByLocNameAsc(area);
                    if (locList.size() > 0) {
                        warehouseTree3.setNodes(Lists.newArrayList());
                    }
                    warehouseTree2.getNodes().add(warehouseTree3);
                }
                warehouseTree1.getNodes().add(warehouseTree2);
            }
            root.getNodes().add(warehouseTree1);
        }
        warehouseTreeList.add(root);
        return warehouseTreeList;
    }

    public Page<Warehouse> findAllByPage(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        Pageable pageable = new PageRequest(pageQuery.getPageNo(), pageQuery.getPageSize());
        return warehouseRepository.findAll(pageable);
    }

    public Warehouse findById(int id) {
        Warehouse warehouse = warehouseRepository.findOne(id);
        Preconditions.checkNotNull(warehouse, "仓库(id:" + id + ")信息不存在");
        return warehouse;
    }

    public Warehouse findByWhName(String whName) {
        Warehouse warehouse = warehouseRepository.findByWhName(whName);
        Preconditions.checkNotNull(warehouse, "仓库(whName:" + whName + ")信息不存在");
        return warehouse;
    }

    public Warehouse findByWhCode(String whCode) {
        Warehouse warehouse = warehouseRepository.findByWhCode(whCode);
        Preconditions.checkNotNull(warehouse, "仓库(whCode:" + whCode + ")信息不存在");
        return warehouse;
    }

    public List<VirtualZone> findVirtualWarehouse(Integer id) {
        List<VirtualZone> virtualZoneList = Lists.newArrayList();
        Warehouse warehouse = this.findById(id);
        List<Zone> zoneList = zoneRepository.findAllByWarehouseOrderByZoneCodeAsc(warehouse);
        for (Zone zone : zoneList) {
            VirtualZone virtualZone = VirtualZone.builder().zoneId(zone.getId()).zoneName(zone.getZoneName()).virtualAreaList(Lists.newArrayList()).build();
            List<Area> areaList = areaRepository.findAllByZoneOrderByAreaNameAsc(zone);
            for (Area area : areaList) {
                VirtualArea virtualArea = VirtualArea.builder().areaId(area.getId()).areaCode(area.getAreaCode()).areaName(area.getAreaName()).virtualLocList(Lists.newArrayList()).build();
                //查找储位，统计使用情况
                int usedNum = 0;
                List<Loc> locList = locRepository.findAllByAreaOrderByLocNameAsc(area);
                for (Loc loc : locList) {
                    VirtualLoc virtualLoc = VirtualLoc.builder().locId(loc.getId()).locCode(loc.getLocCode()).locName(loc.getLocName()).inventoryList(Lists.newArrayList()).build();
                    //查找物料汇总信息
                    List<Inventory> inventoryList = inventoryRepository.findAllByWhNameAndLocName(warehouse.getWhName(), loc.getLocName());
                    virtualLoc.setUseFlag(CollectionUtils.isNotEmpty(inventoryList));
                    usedNum = CollectionUtils.isNotEmpty(inventoryList) ? usedNum + 1 : usedNum;
                    virtualLoc.getInventoryList().addAll(inventoryList);
                    virtualArea.getVirtualLocList().add(virtualLoc);
                }
                //统计储位使用数量，和总数
                virtualArea.setUsedNum(usedNum);
                virtualArea.setTotalNum(locList.size());
                virtualZone.getVirtualAreaList().add(virtualArea);
            }
            virtualZoneList.add(virtualZone);
        }
        return virtualZoneList;
    }
}
