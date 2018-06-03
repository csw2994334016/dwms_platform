package com.three.dwms.utils;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/6/3.
 * Description:
 */
public class TimeCriteriaUtil {

    public static void timePredication(HttpServletRequest request, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Path<Date> createTime) {
        if (StringUtils.isNotBlank(request.getParameter("startTime")) && StringUtils.isNotBlank(request.getParameter("endTime"))) {
            Date st = StringUtil.toDate(request.getParameter("startTime"));
            Date et = StringUtil.toDate(request.getParameter("endTime"));
            if (st != null && et != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(createTime, st));
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(createTime, et));
            }
        } else if (StringUtils.isNotBlank(request.getParameter("startTime"))) {
            Date st = StringUtil.toDate(request.getParameter("startTime"));
            if (st != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(createTime, st));
            }
        } else if (StringUtils.isNotBlank(request.getParameter("endTime"))) {
            Date et = StringUtil.toDate(request.getParameter("endTime"));
            if (et != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(createTime, et));
            }
        }
    }
}
