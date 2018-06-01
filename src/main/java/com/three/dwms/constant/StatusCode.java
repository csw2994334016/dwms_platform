package com.three.dwms.constant;

/**
 * Created by csw on 2018/3/28.
 * Description:
 */
public enum StatusCode {

    NORMAL(1, "正常"), DISABLE(0, "禁用"), DELETE(2, "删除");

    private int code;
    private String desc;

    StatusCode(int code, String desc) {
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
