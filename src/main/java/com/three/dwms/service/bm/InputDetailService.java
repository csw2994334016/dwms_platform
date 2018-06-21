package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.InputStateCode;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.*;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.entity.bm.Inventory;
import com.three.dwms.exception.ParamException;
import com.three.dwms.param.bm.InputDetailParam;
import com.three.dwms.repository.basic.*;
import com.three.dwms.repository.bm.InputDetailRepository;
import com.three.dwms.repository.bm.InventoryRepository;
import com.three.dwms.service.basic.AreaService;
import com.three.dwms.service.basic.LocService;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.service.basic.ZoneService;
import com.three.dwms.utils.*;
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
        String[] str = {"batchNo", "whName", "locName", "skuDesc", "spec", "categoryName", "unitName", "unitPrice", "amount", "totalPrice", "purchaseDept", "purchaser", "receiver", "supplierName", "remark"};
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
        String inputNo = StringUtil.getCurCode("R", maxNo);
        //批次号验证
        InputDetailParam first = null;
        if (paramList.size() > 0) {
            first = paramList.get(0);
        }
        for (InputDetailParam param : paramList) {
            if ("import".equals(param.getInputType())) {
                if (StringUtils.isNotBlank(param.getBatchNo())) {
                    if (!param.getBatchNo().equals(first.getBatchNo())) {
                        throw new ParamException("EXCEL导入每条数据批次号必须相同");
                    }
                } else {
                    throw new ParamException("EXCEL导入批次号不可以为空");
                }
            }
        }
        String batchNo = StringUtil.getCurDateStr();
        if (first != null && "import".equals(first.getInputType())) {
            if (checkBatchNoExist(first.getBatchNo())) {
                throw new ParamException("系统检测该Excel批次号已导入，请检查后重新导入");
            }
            batchNo = first.getBatchNo();
        }
        //验证此次导入是否拥有仓库权限
        for (InputDetailParam param : paramList) {
            BeanValidator.check(param);
            Warehouse warehouse = warehouseRepository.findByWhName(param.getWhName());
            Preconditions.checkNotNull(warehouse, "库房信息不存在");
            List<String> whCodeList = Arrays.asList(StringUtils.split(RequestHolder.getCurrentUser().getWhCodes(), ","));
            if (!whCodeList.contains(warehouse.getWhCode())) {
                throw new ParamException("当前用户没有操作库房(whName:" + param.getWhName() + ")的权限");
            }
        }
        for (InputDetailParam param : paramList) {
            InputDetail inputDetail = InputDetail.builder().build();
            //自动生成入库单编号
            inputDetail.setInputNo(inputNo);
            inputDetail.setBatchNo(batchNo);
            //物料信息
            Product product = productRepository.findBySkuDescAndSpec(param.getSkuDesc(), param.getSpec());
            if (product == null) {
                String categoryName = StringUtils.isBlank(param.getCategoryName()) ? "其它" : param.getCategoryName();
                Category category = categoryRepository.findByName(categoryName);
                if (category == null) {
                    Category category1 = Category.builder().name(categoryName).build();
                    category1.setRemark("入库自动生成");
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
                product1.setRemark("入库自动生成");
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
                unit1.setRemark("入库自动生成");
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
                    supplier1.setRemark("入库自动生成");
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
            //状态
            inputDetail.setState(InputStateCode.INPUT.getCode());
            inputDetailList.add(inputDetail);
        }
        inputDetailRepository.save(inputDetailList);
        //所有单子入库成功后，统一对inventory表操作
        for (InputDetail inputDetail : inputDetailList) {
            Inventory inventory = inventoryRepository.findBySkuAndWhCodeAndLocName(inputDetail.getSku(), inputDetail.getWhCode(), inputDetail.getLocName());
            if (inventory == null) {
                Inventory inventory1 = Inventory.builder().sku(inputDetail.getSku()).whCode(inputDetail.getWhCode()).locName(inputDetail.getLocName()).skuDesc(inputDetail.getSkuDesc()).spec(inputDetail.getSpec()).whName(inputDetail.getWhName()).skuAmount(inputDetail.getAmount()).build();
                inventory1.setRemark("入库自动生成");
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
        }
    }

    private boolean checkBatchNoExist(String batchNo) {
        return inputDetailRepository.countByBatchNo(batchNo) > 0;
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
                CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            inputDetailList = inputDetailRepository.findAll(specification);
        }
        return inputDetailList;
    }

    public List<InputDetail> findAll() {
        return this.stockQuery();
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<InputDetail> inputDetails = Lists.newArrayList();
        List<Inventory> inventories = Lists.newArrayList();
        for (Integer id : ids) {
            InputDetail inputDetail = this.findById(id);
            inputDetails.add(inputDetail);
            Inventory inventory = inventoryRepository.findBySkuAndWhCodeAndLocName(inputDetail.getSku(), inputDetail.getWhCode(), inputDetail.getLocName());
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
        Preconditions.checkNotNull(inputDetail, "入库单(id:" + id + ")详情不存在");
        return inputDetail;
    }

    public List<InputDetail> inputStatics() {
        return null;
    }
}
