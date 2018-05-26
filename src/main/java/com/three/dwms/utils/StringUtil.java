package com.three.dwms.utils;


import com.three.dwms.exception.ParamException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
@Slf4j
public class StringUtil {

    public static final String excel2003 = ".xls";
    public static final String excel2007 = ".xlsx";

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
        return  pre + String.format("%06d", max);
    }
}
