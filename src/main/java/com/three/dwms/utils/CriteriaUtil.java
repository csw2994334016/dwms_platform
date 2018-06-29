package com.three.dwms.utils;

import com.google.common.collect.Lists;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.param.statics.Statics;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2018/6/3.
 * Description:
 */
public class CriteriaUtil {

    public static void getDatePredicate(HttpServletRequest request, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Path<Date> createTime) {
        if (StringUtils.isNotBlank(request.getParameter("startTime")) && StringUtils.isNotBlank(request.getParameter("endTime"))) {
            Date st = StringUtil.getStrToDate(request.getParameter("startTime"));
            Date et = StringUtil.getStrToDate(request.getParameter("endTime"));
            if (st != null && et != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(createTime, st));
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(createTime, et));
            }
        } else if (StringUtils.isNotBlank(request.getParameter("startTime"))) {
            Date st = StringUtil.getStrToDate(request.getParameter("startTime"));
            if (st != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(createTime, st));
            }
        } else if (StringUtils.isNotBlank(request.getParameter("endTime"))) {
            Date et = StringUtil.getStrToDate(request.getParameter("endTime"));
            if (et != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(createTime, et));
            }
        }
    }

    public static Predicate getPredicate(HttpServletRequest request, CriteriaBuilder criteriaBuilder, Path<Object> proposer, Path<Object> approver, Path<Object> state, Path<Date> createTime) {
        List<Predicate> predicateList = Lists.newArrayList();
        if (StringUtils.isNotBlank(request.getParameter("proposer"))) {
            predicateList.add(criteriaBuilder.equal(proposer, request.getParameter("proposer")));
        } else {
            predicateList.add(criteriaBuilder.equal(proposer, RequestHolder.getCurrentUser().getUsername()));
        }
        if (StringUtils.isNotBlank(request.getParameter("approver"))) {
            predicateList.add(criteriaBuilder.equal(approver, request.getParameter("approver")));
        }
        if (StringUtils.isNotBlank(request.getParameter("state"))) {
            predicateList.add(criteriaBuilder.equal(state, Integer.valueOf(request.getParameter("state"))));
        }
        CriteriaUtil.getDatePredicate(request, criteriaBuilder, predicateList, createTime);
        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    public static Statics createStatics(String startTime, String endTime, Map<String, Double> dayNumberMap, Statics statics) {
        for (String day : StringUtil.getMonthDays(startTime, endTime)) {
            statics.getLabelList().add(day);
            if (dayNumberMap.get(day) == null) {
                statics.getDataList().add(0.0);
            } else {
                statics.getDataList().add(dayNumberMap.get(day));
            }
        }
        return statics;
    }
}
