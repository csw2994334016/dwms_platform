package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
public enum WhTypeCode {

    ROOT(0, "仓库资料"), WAREHOUSE(1, "仓库"), ZONE(2, "仓区"), AREA(3, "储区"), LOC(4, "储位");

    private int code;
    private String desc;

    WhTypeCode(int code, String desc) {
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
