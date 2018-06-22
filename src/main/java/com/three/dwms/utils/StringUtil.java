package com.three.dwms.utils;


import com.google.common.collect.Lists;
import com.three.dwms.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
@Slf4j
public class StringUtil {

    public static final String excel2003 = ".xls";
    public static final String excel2007 = ".xlsx";

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Integer> splitToIntListByReg(String userIds, String reg) {
        List<Integer> userIdList = new ArrayList<>();
        try {
            for (String str : userIds.split(reg)) {
                userIdList.add(Integer.valueOf(str.trim()));
            }
        } catch (Exception e) {
            log.error("接口传入数据格式不正确，解析异常: {}", e);
            throw new ParamException("接口传入数据格式不正确，解析异常");
        }
        return userIdList;
    }

    // 是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static String getCurCode(String pre, String maxCode) {
        int max = 1;
        if (maxCode != null) {
            max = Integer.valueOf(maxCode.substring(1)) + 1;
        }
        return pre + String.format("%06d", max);
    }

    public static Date toDate(String time) {
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurDateStr() {
        return sdf.format(new Date());
    }

    public static String getStartTime(String year, String month) {
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
            return year + "-" + month + "-" + "01";
        } else {
            String curTime = getCurDateStr();
            String[] curTimes = curTime.split("-");
            return curTimes[0] + "-" + curTimes[1] + "-" + "01";
        }
    }

    public static String getEndTime(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
            calendar.set(Integer.parseInt(year), Integer.parseInt(month), 0);
            return year + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            String curTime = getCurDateStr();
            String[] curTimes = curTime.split("-");
            calendar.setTime(new Date());
            return curTimes[0] + "-" + curTimes[1] + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        }
    }

    public static String getDateToStr(Date createTime) {
        return sdf.format(createTime);
    }

    public static List<String> getMonthDays(String startTime, String endTime) {
        List<String> dayList = Lists.newArrayList();
        int st = Integer.parseInt(startTime.split("-")[2]);
        int et = Integer.parseInt(endTime.split("-")[2]);
        for (int i = st; i <= et; i++) {
            dayList.add(String.format("%02d", i));
        }
        return dayList;
    }
}
