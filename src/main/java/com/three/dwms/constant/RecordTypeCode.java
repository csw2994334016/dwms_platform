package com.three.dwms.constant;

/**
 * Created by csw on 2017/12/9.
 * Description:
 */
public enum RecordTypeCode {

    SCRAP(1, "报废"), LOSS(2, "损耗"),
    MISSING(3, "丢失"), OTHER(4, "其它");

    private Integer code;
    private String desc;

    RecordTypeCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
