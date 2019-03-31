package com.three.dwms.service.bm;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.StatusCode;
import com.three.dwms.entity.basic.Warehouse;
import com.three.dwms.entity.bm.ExportRecord;
import com.three.dwms.entity.bm.InputDetail;
import com.three.dwms.param.bm.ExportRecordParam;
import com.three.dwms.repository.bm.ExportRecordRepository;
import com.three.dwms.repository.bm.InputDetailRepository;
import com.three.dwms.service.basic.WarehouseService;
import com.three.dwms.service.export.ExportData;
import com.three.dwms.service.export.ExportExcel;
import com.three.dwms.service.export.Record;
import com.three.dwms.utils.CriteriaUtil;
import com.three.dwms.utils.IpUtil;
import com.three.dwms.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@Service
public class ExportRecordService {

    @Value("#{props['init.waterContent']}")
    private String waterContent;

    @Value("#{props['init.exportTitle']}")
    private String exportTitle;

    @Autowired
    private ExportRecordRepository exportRecordRepository;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InputDetailRepository inputDetailRepository;

    public List<ExportRecord> findAll() {
        List<ExportRecord> exportRecordList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<ExportRecord> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("exportNo"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("exportNo"), request.getParameter("exportNo")));
                }
                predicateList.add(criteriaBuilder.equal(root.get("purchaser"), RequestHolder.getCurrentUser().getUsername()));
                CriteriaUtil.getDatePredicate2(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            exportRecordList = exportRecordRepository.findAll(specification);
        }
        return exportRecordList;
    }

    public List<InputDetail> findInputDetails() {
        List<InputDetail> inputDetailList = Lists.newArrayList();
        if (RequestHolder.getCurrentUser() == null) {
            return inputDetailList;
        }
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<InputDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("whId"))) {
                    Warehouse warehouse = warehouseService.findById(Integer.valueOf(request.getParameter("whId")));
                    predicateList.add(criteriaBuilder.equal(root.get("whCode"), warehouse.getWhCode()));
                }
                predicateList.add(criteriaBuilder.equal(root.get("purchaser"), RequestHolder.getCurrentUser().getRealName()));
                CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            inputDetailList = inputDetailRepository.findAll(specification);
        }
        return inputDetailList;
    }

    @Transactional
    public String exportRecord(List<ExportRecordParam> paramList) {
        String fileName = "";
        List<Integer> ids = Lists.newArrayList();
        for (ExportRecordParam param : paramList) {
            ids.add(param.getId());
        }
        List<InputDetail> inputDetailList = inputDetailRepository.findByIdIn(ids);
        String maxCode = exportRecordRepository.findMaxExportCode();
        String exportNo = StringUtil.getCurCode1("E", 8, maxCode);
        String userName = RequestHolder.getCurrentUser().getUsername();
        String title = exportNo + "--" + exportTitle + "--" + userName;
        double totalMoney = 0;
        List<Record> recordList = Lists.newArrayList();
        for (InputDetail inputDetail : inputDetailList) {
            Record record = Record.builder().whName(inputDetail.getWhName()).skuDesc(inputDetail.getSkuDesc()).spec(inputDetail.getSpec()).unitName(inputDetail.getUnitName()).purchaser(inputDetail.getPurchaser()).build();
            record.setUnitPrice(inputDetail.getUnitPrice().toString());
            record.setAmount(inputDetail.getAmount().toString());
            record.setTotalPrice(inputDetail.getTotalPrice().toString());
            totalMoney += inputDetail.getTotalPrice();
            record.setCreateTime(StringUtil.getCurDateStrByPattern("yyyy/MM/dd"));
            recordList.add(record);
        }
        // 存储到数据库
        ExportRecord exportRecord = ExportRecord.builder().exportNo(exportNo).purchaser(userName).totalMoney(totalMoney).build();
        exportRecord.setRemark("导出记录自动生成");
        exportRecord.setStatus(StatusCode.NORMAL.getCode());
        exportRecord.setCreator(RequestHolder.getCurrentUser().getUsername());
        exportRecord.setCreateTime(new Date());
        exportRecord.setOperator(RequestHolder.getCurrentUser().getUsername());
        exportRecord.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        exportRecord.setOperateTime(new Date());

        exportRecord = exportRecordRepository.save(exportRecord);

        // 导出表格
        if (exportRecord != null) {
            ExportData exportData = ExportData.builder().title(title).recordList(recordList).totalMoney(String.valueOf(totalMoney)).build();
            exportData.setPurchaser(userName);
            exportData.setWaterContent(waterContent);
            exportData.setExportTitle(exportTitle);
            ExportExcel exportExcel = new ExportExcel();
            fileName = exportExcel.export(exportData);
        }
        return fileName;
    }
}
