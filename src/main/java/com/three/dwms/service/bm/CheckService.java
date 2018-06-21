package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Product;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.bm.Check;
import com.three.dwms.entity.bm.CheckDetail;
import com.three.dwms.param.bm.CheckDetailParam;
import com.three.dwms.param.bm.CheckParam;
import com.three.dwms.repository.bm.CheckDetailRepository;
import com.three.dwms.repository.bm.CheckRepository;
import com.three.dwms.service.basic.ProductService;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.utils.CriteriaUtil;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
@Service
public class CheckService {

    @Resource
    private CheckRepository checkRepository;

    @Resource
    private CheckDetailRepository checkDetailRepository;

    @Resource
    private ProductService productService;

    @Resource
    private WarehouseService warehouseService;

    public void create(CheckParam param) {

        List<CheckDetail> checkDetailList = Lists.newArrayList();
        for (CheckDetailParam checkDetailParam : param.getCheckDetailParamList()) {
            Product product = productService.findBySku(checkDetailParam.getSku());
            Warehouse warehouse = warehouseService.findByWhCode(checkDetailParam.getWhCode());
            CheckDetail checkDetail = CheckDetail.builder().sku(checkDetailParam.getSku()).skuDesc(product.getSkuDesc()).spec(product.getSpec()).whCode(checkDetailParam.getWhCode()).whName(warehouse.getWhName()).storeAmount(checkDetailParam.getStoreAmount()).checkAmount(checkDetailParam.getCheckAmount()).difference(checkDetailParam.getDifference()).build();
            checkDetail.setRemark(param.getRemark());
            checkDetail.setStatus(StatusCode.NORMAL.getCode());
            checkDetail.setCreator(RequestHolder.getCurrentUser().getUsername());
            checkDetail.setCreateTime(new Date());
            checkDetail.setOperator(RequestHolder.getCurrentUser().getUsername());
            checkDetail.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            checkDetail.setOperateTime(new Date());
            checkDetailList.add(checkDetail);
        }

        String maxNo = checkRepository.findMaxCheckNo();
        String checkNo = StringUtil.getCurCode("C", maxNo);
        Check check = Check.builder().checkNo(checkNo).checkUser(RequestHolder.getCurrentUser().getUsername()).build();
        check.setRemark(param.getRemark());
        check.setStatus(StatusCode.NORMAL.getCode());
        check.setCreator(RequestHolder.getCurrentUser().getUsername());
        check.setCreateTime(new Date());
        check.setOperator(RequestHolder.getCurrentUser().getUsername());
        check.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        check.setOperateTime(new Date());

        check = checkRepository.save(check);

        for (CheckDetail checkDetail : checkDetailList) {
            checkDetail.setCheck(check);
        }
        checkDetailRepository.save(checkDetailList);
    }

    public List<Check> findAll() {
        List<Check> checkList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Check> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("checkUser"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("checkUser"), request.getParameter("checkUser")));
                }
                CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            checkList = checkRepository.findAll(specification);
        }
        return checkList;
    }

    public List<CheckDetail> findCheckDetail() {
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null && request.getParameter("id") != null) {
            Check check = this.findById(Integer.valueOf(request.getParameter("id")));
            return checkDetailRepository.findAllByCheck(check);
        }
        return Lists.newArrayList();
    }

    private Check findById(Integer id) {
        Check check = checkRepository.findOne(id);
        Preconditions.checkNotNull(check, "盘点单(id:" + id + ")不存在");
        return check;
    }
}
