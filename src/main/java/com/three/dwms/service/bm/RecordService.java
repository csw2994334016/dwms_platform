package com.three.dwms.service.bm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.constant.RecordTypeCode;
import com.three.dwms.entity.bm.Record;
import com.three.dwms.param.bm.RecordParam;
import com.three.dwms.param.bm.StateCode;
import com.three.dwms.repository.bm.RecordRepository;
import com.three.dwms.utils.BeanValidator;
import com.three.dwms.utils.CriteriaUtil;
import com.three.dwms.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/6/23.
 * Description:
 */
@Service
public class RecordService {

    @Resource
    private RecordRepository recordRepository;

    @Transactional
    public void create(RecordParam param) {
        BeanValidator.check(param);

        Record record = Record.builder().recordDate(new Date()).whName(param.getWhName()).sku(param.getSku()).skuDesc(param.getSkuDesc()).spec(param.getSpec()).recordAmount(param.getRecordAmount()).recordType(param.getRecordType()).person(param.getPerson()).reason(param.getReason()).build();
        record.setStatus(param.getStatus());
        record.setRemark(param.getRemark());
        record.setCreator(RequestHolder.getCurrentUser().getUsername());
        record.setCreateTime(new Date());
        record.setOperator(RequestHolder.getCurrentUser().getUsername());
        record.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        record.setOperateTime(new Date());

        recordRepository.save(record);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        List<Record> recordList = Lists.newArrayList();
        for (Integer id : ids) {
            recordList.add(this.findById(id));
        }
        recordRepository.delete(recordList);
    }

    public Record update(RecordParam param) {
        Record record = this.findById(param.getId());
        BeanValidator.check(param);

        record.setRecordDate(new Date());
        record.setWhName(param.getWhName());
        record.setSku(param.getSku());
        record.setSkuDesc(param.getSkuDesc());
        record.setSpec(param.getSpec());
        record.setRecordAmount(param.getRecordAmount());
        record.setRecordType(param.getRecordType());
        record.setPerson(param.getPerson());
        record.setReason(param.getReason());

        record.setOperator(RequestHolder.getCurrentUser().getUsername());
        record.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        record.setOperateTime(new Date());

        return recordRepository.save(record);
    }

    private Record findById(Integer id) {
        Record record = recordRepository.findOne(id);
        Preconditions.checkNotNull(record, "缺失记录(id:" + id + ")不存在");
        return record;
    }

    public List<Record> findAll() {
        List<Record> recordList = Lists.newArrayList();
        HttpServletRequest request = RequestHolder.getCurrentRequest();
        if (request != null) {
            Specification<Record> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                if (StringUtils.isNotBlank(request.getParameter("whName"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("whName"), request.getParameter("whName")));
                }
                if (StringUtils.isNotBlank(request.getParameter("sku"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("sku"), request.getParameter("sku")));
                }
                if (StringUtils.isNotBlank(request.getParameter("recordType"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("recordType"), request.getParameter("recordType")));
                }
                CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, root.get("createTime"));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            };
            recordList = recordRepository.findAll(specification);
        }
        return recordList;
    }

    public List<StateCode> findRecordType() {
        List<StateCode> stateCodeList = Lists.newArrayList();
        for (RecordTypeCode stateCode : RecordTypeCode.values()) {
            StateCode state = StateCode.builder().code(stateCode.getCode()).desc(stateCode.getDesc()).build();
            stateCodeList.add(state);
        }
        return stateCodeList;
    }
}
