package com.three.dwms.service.bm;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.repository.bm.InputDetailRepository;
import com.three.dwms.utils.ImportExcel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by csw on 2018/5/22.
 * Description:
 */
@Service
public class InputDetailService {

    @Resource
    private InputDetailRepository inputDetailRepository;

    public boolean batchImport(String filename, MultipartFile file) {
        boolean leadInOk = false;
        //创建处理EXCEL
        ImportExcel<InputDetail> importExcel = new ImportExcel<>();
        //解析excel，获取客户信息集合
        String[] str = {"inputDate", "whName", "skuDesc", "spec", "unit", "unitPrice", "amount", "totalPrice", "purchaseDept", "purchaser", "receiver", "supplierName", "remark"};
        List<String> attributes = Arrays.asList(str);
        List<InputDetail> inputDetailList = importExcel.leadInExcel(filename, file, InputDetail.class, attributes);

        if (inputDetailList != null) {
            leadInOk = true;
        }

        //存入数据库

        return leadInOk;
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
}
