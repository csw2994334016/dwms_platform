package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/26.
 * Description:
 */
public enum InputStateCode {

    INPUT(6, "入库"),
    NOT_INPUT(7, "未入库"),
    TRANSFER(8, "转移"),
    CANCEL(12, "作废");

    private int code;
    private String desc;

    InputStateCode(int code, String desc) {
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
