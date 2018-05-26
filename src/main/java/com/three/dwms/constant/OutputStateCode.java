package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/26.
 * Description:
 */
public enum OutputStateCode {

    DRAFT(1, "草稿"),
    APPLY(2, "申请"),
    APPROVE(3, "审批通过"),
    DECLINE(4, "审批不通过"),
    OUTPUT(5, "出货"),
    CANCEL(12, "作废");

    private int code;
    private String desc;

    OutputStateCode(int code, String desc) {
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
