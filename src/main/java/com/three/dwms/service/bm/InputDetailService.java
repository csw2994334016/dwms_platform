package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.InputStateCode;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.*;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.param.bm.InputDetailParam;
import com.three.dwms.repository.basic.*;
import com.three.dwms.repository.bm.InputDetailRepository;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.service.basic.AreaService;
import com.three.dwms.service.basic.LocService;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.service.basic.ZoneService;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.ImportExcel;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@Service
public class InputDetailService {

    @Resource
    private InputDetailRepository inputDetailRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CategoryRepository categoryRepository;

    @Resource
    private UnitRepository unitRepository;

    @Resource
    private SupplierRepository supplierRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private LocService locService;

    @Resource
    private InventoryRepository inventoryRepository;

    @Resource
    private ZoneService zoneService;

    @Resource
    private AreaService areaService;

    public List<InputDetail> batchImport(String filename, MultipartFile file) {
        //创建处理EXCEL
        ImportExcel<InputDetail> importExcel = new ImportExcel<>();
        //解析excel，获取客户信息集合
        String[] str = {"whName", "locName", "skuDesc", "spec", "categoryName", "unitName", "unitPrice", "amount", "totalPrice", "purchaseDept", "purchaser", "receiver", "supplierName", "remark"};
        List<String> attributes = Arrays.asList(str);
        List<InputDetail> inputDetailList = importExcel.leadInExcel(filename, file, InputDetail.class, attributes);

        if (CollectionUtils.isNotEmpty(inputDetailList)) {
            return inputDetailList;
        }
        return Lists.newArrayList();
    }

    @Transactional
    public void create(List<InputDetailParam> paramList) {
        List<InputDetail> inputDetailList = Lists.newArrayList();
        String maxNo = inputDetailRepository.findMaxInputNo();
        String inputNo = StringUtil.getCurCode("I", maxNo);
        for (InputDetailParam param : paramList) {
            BeanValidator.check(param);
            InputDetail inputDetail = InputDetail.builder().build();
            //自动生成入库单编号
            inputDetail.setInputNo(inputNo);
            //物料信息
            Product product = productRepository.findBySkuDescAndSpec(param.getSkuDesc(), param.getSpec());
            if (product == null) {
                String categoryName = StringUtils.isBlank(param.getCategoryName()) ? "其他" : param.getCategoryName();
                Category category = categoryRepository.findByName(categoryName);
                if (category == null) {
                    Category category1 = Category.builder().name(param.getCategoryName()).build();
                    category1.setRemark("导入入库自动生成");
                    category1.setStatus(StatusCode.NORMAL.getCode());
                    category1.setCreator(RequestHolder.getCurrentUser().getUsername());
                    category1.setCreateTime(new Date());
                    category1.setOperator(RequestHolder.getCurrentUser().getUsername());
                    category1.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    category1.setOperateTime(new Date());
                    category = categoryRepository.save(category1);
                }
                String maxCode = productRepository.findMaxSku();
                String sku = StringUtil.getCurCode("P", maxCode);
                Product product1 = Product.builder().sku(sku).skuDesc(param.getSkuDesc()).spec(param.getSpec()).category(category).build();
                product1.setRemark("导入入库自动生成物");
                product1.setStatus(StatusCode.NORMAL.getCode());
                product1.setCreator(RequestHolder.getCurrentUser().getUsername());
                product1.setCreateTime(new Date());
                product1.setOperator(RequestHolder.getCurrentUser().getUsername());
                product1.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                product1.setOperateTime(new Date());
                product = productRepository.save(product1);
            }
            inputDetail.setSku(product.getSku());
            inputDetail.setSkuDesc(product.getSkuDesc());
            inputDetail.setSpec(product.getSpec());
            //单位
            Unit unit = unitRepository.findByUnitName(param.getUnitName());
            if (unit == null) {
                String maxCode = unitRepository.findMaxUnitCode();
                String unitCode = StringUtil.getCurCode("U", maxCode);
                Unit unit1 = Unit.builder().unitCode(unitCode).unitName(param.getUnitName()).build();
                unit1.setRemark("导入入库自动生成");
                unit1.setStatus(StatusCode.NORMAL.getCode());
                unit1.setCreator(RequestHolder.getCurrentUser().getUsername());
                unit1.setCreateTime(new Date());
                unit1.setOperator(RequestHolder.getCurrentUser().getUsername());
                unit1.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                unit1.setOperateTime(new Date());
                unit = unitRepository.save(unit1);
            }
            inputDetail.setUnitName(unit.getUnitName());
            inputDetail.setUnitPrice(param.getUnitPrice());
            inputDetail.setAmount(param.getAmount());
            inputDetail.setTotalPrice(param.getTotalPrice());
            inputDetail.setPurchaseDept(param.getPurchaseDept());
            inputDetail.setPurchaser(param.getPurchaser());
            inputDetail.setReceiver(param.getReceiver());
            //供应商
            inputDetail.setSupplierName(param.getSupplierName());
            if (StringUtils.isNotBlank(param.getSupplierName())) {
                Supplier supplier = supplierRepository.findBySupplierName(param.getSupplierName());
                if (supplier == null) {
                    Supplier supplier1 = Supplier.builder().supplierCode(param.getSupplierName()).supplierName(param.getSupplierName()).build();
                    supplier1.setRemark("导入入库自动生成");
                    supplier1.setStatus(StatusCode.NORMAL.getCode());
                    supplier1.setCreator(RequestHolder.getCurrentUser().getUsername());
                    supplier1.setCreateTime(new Date());
                    supplier1.setOperator(RequestHolder.getCurrentUser().getUsername());
                    supplier1.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    supplier1.setOperateTime(new Date());
                    supplier = supplierRepository.save(supplier1);
                }
                inputDetail.setSupplierName(supplier.getSupplierName());
            }
            //库房
            Warehouse warehouse = warehouseRepository.findByWhName(param.getWhName());
            Preconditions.checkNotNull(warehouse, "库房信息不存在");
            inputDetail.setWhCode(warehouse.getWhCode());
            inputDetail.setWhName(warehouse.getWhName());
            //储位代码
            Loc loc = locService.findByLocNameAndWarehouse(param.getLocName(), warehouse);
            inputDetail.setLocCode(loc.getLocCode());
            inputDetail.setLocName(loc.getLocName());
            inputDetail.setAreaCode(loc.getArea().getAreaCode());
            inputDetail.setZoneCode(loc.getArea().getZone().getZoneCode());
            inputDetail.setRemark(param.getRemark());
            inputDetail.setStatus(StatusCode.NORMAL.getCode());
            inputDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
            inputDetail.setCreateTime(new Date());
            inputDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
            inputDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            inputDetail.setOperateTime(new Date());
            //inventory表的操作
            Inventory inventory = inventoryRepository.findBySkuAndWhCodeAndLocName(product.getSku(), warehouse.getWhCode(), inputDetail.getLocName());
            if (inventory == null) {
                Inventory inventory1 = Inventory.builder().sku(product.getSku()).whCode(warehouse.getWhCode()).locName(inputDetail.getLocName()).skuDesc(product.getSkuDesc()).spec(product.getSpec()).whName(warehouse.getWhName()).skuAmount(inputDetail.getAmount()).build();
                inventory1.setRemark("导入入库自动生成");
                inventory1.setStatus(StatusCode.NORMAL.getCode());
                inventory1.setCreator(RequestHolder.getCurrentUser().getUsername());
                inventory1.setCreateTime(new Date());
                inventory1.setOperator(RequestHolder.getCurrentUser().getUsername());
                inventory1.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                inventory1.setOperateTime(new Date());
                inventoryRepository.save(inventory1);
            } else {
                inventory.setSkuAmount(inventory.getSkuAmount() + inputDetail.getAmount());
                inventoryRepository.save(inventory);
            }
            //状态
            inputDetail.setState(InputStateCode.NOT_INPUT.getCode());
            inputDetailList.add(inputDetail);
        }
        inputDetailRepository.save(inputDetailList);
    }

    public List<InputDetail> stockQuery() {
        List<InputDetail> inputDetailList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<InputDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("whId"))) {
                    Warehouse warehouse = warehouseService.findById(Integer.valueOf(request.getParameter("whId")));
                    predicateList.add(criteriaBuilder.equal(root.get("whCode"), warehouse.getWhCode()));
                }
                if (StringUtils.isNotBlank(request.getParameter("zoneId"))) {
                    Zone zone = zoneService.findById(Integer.valueOf(request.getParameter("zoneId")));
                    predicateList.add(criteriaBuilder.equal(root.get("zoneCode"), zone.getZoneCode()));
                }
                if (StringUtils.isNotBlank(request.getParameter("areaId"))) {
                    Area area = areaService.findById(Integer.valueOf(request.getParameter("areaId")));
                    predicateList.add(criteriaBuilder.equal(root.get("areaCode"), area.getAreaCode()));
                }
                if (StringUtils.isNotBlank(request.getParameter("locId"))) {
                    Loc loc = locService.findById(Integer.valueOf(request.getParameter("locId")));
                    predicateList.add(criteriaBuilder.equal(root.get("locName"), loc.getLocName()));
                }
                if (StringUtils.isNotBlank(request.getParameter("sku"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("sku"), request.getParameter("sku")));
                }
                if (StringUtils.isNotBlank(request.getParameter("purchaseDept"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("purchaseDept"), request.getParameter("purchaseDept")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            inputDetailList = inputDetailRepository.findAll(specification);
        }
        return inputDetailList;
    }

    public List<InputDetail> findAll() {
        return (List<InputDetail>) inputDetailRepository.findAll();
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<InputDetail> inputDetails = Lists.newArrayList();
        List<Inventory> inventories = Lists.newArrayList();
        for (Integer id : ids) {
            InputDetail inputDetail = this.findById(id);
            inputDetails.add(inputDetail);
            Inventory inventory = inventoryRepository.findBySkuAndWhCode(inputDetail.getSku(), inputDetail.getWhCode());
            if (inventory != null) {
                inventory.setSkuAmount(inventory.getSkuAmount() - inputDetail.getAmount());
                inventories.add(inventory);
            }
        }
        inputDetailRepository.delete(inputDetails);
        //删除Inventory表中相应的数量
        inventoryRepository.save(inventories);
    }

    private InputDetail findById(Integer id) {
        InputDetail inputDetail = inputDetailRepository.findOne(id);
        Preconditions.checkNotNull(inputDetail, "入库单详情不存在");
        return inputDetail;
    }
}
