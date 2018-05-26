package com.three.dwms.service.bm;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StateCode;
import com.three.dwms.entity.basic.Category;
import com.three.dwms.entity.basic.Product;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.param.bm.InputDetailParam;
import com.three.dwms.repository.basic.CategoryRepository;
import com.three.dwms.repository.basic.ProductRepository;
import com.three.dwms.repository.bm.InputDetailRepository;
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

    public List<InputDetail> batchImport(String filename, MultipartFile file) {
        //创建处理EXCEL
        ImportExcel<InputDetail> importExcel = new ImportExcel<>();
        //解析excel，获取客户信息集合
        String[] str = {"inputDate", "whName", "skuDesc", "spec", "categoryName", "unitName", "unitPrice", "amount", "totalPrice", "purchaseDept", "purchaser", "receiver", "supplierName", "remark"};
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
        for (InputDetailParam param : paramList) {
            BeanValidator.check(param);
            InputDetail inputDetail = InputDetail.builder().build();
            Product product = productRepository.findBySkuDescAndSpec(param.getSkuDesc(), param.getSpec());
            if (product == null) {
                String categoryName = StringUtils.isBlank(param.getCategoryName()) ? "其他" : param.getCategoryName();
                Category category = categoryRepository.findByName(categoryName);
                if (category == null) {
                    Category category1 = Category.builder().name(param.getCategoryName()).build();
                    category1.setRemark("导入入库单时新增的物料类别");
                    category1.setStatus(StateCode.NORMAL.getCode());
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
                product1.setRemark("导入入库单时新增的物料信息");
                product1.setStatus(StateCode.NORMAL.getCode());
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

        }
    }

    public List<InputDetail> stockQuery() {
        List<InputDetail> inputDetailList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<InputDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (request.getParameter("zoneCode") != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("zoneCode"), request.getParameter("zoneCode")));
                }
                if (request.getParameter("areaCode") != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("areaCode"), request.getParameter("areaCode")));
                }
                if (request.getParameter("locCode") != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("locCode"), request.getParameter("locCode")));
                }
                if (request.getParameter("sku") != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("sku"), request.getParameter("sku")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            inputDetailList = inputDetailRepository.findAll(specification);
        }
        return inputDetailList;
    }

    public List<InputDetail> findAll() {
        return null;
    }
}
