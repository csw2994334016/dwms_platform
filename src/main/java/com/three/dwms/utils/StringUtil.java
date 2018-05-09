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
}
