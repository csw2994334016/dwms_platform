package com.three.dwms.constant;

/**
 * Created by csw on 2018/3/20.
 * Description:
 */
public enum  LogTypeCode {

    TYPE_DEPT(1, "部门"), TYPE_USER(2, "用户");

    private int code;
    private String desc;

    LogTypeCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
