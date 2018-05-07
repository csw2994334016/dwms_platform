package com.three.dwms.constant;

/**
 * Created by csw on 2017/12/9.
 * Description:
 */
public enum ResultCode {

    SUCCESS(0, "操作成功"), FAIL(1, "操作失败");

    private Integer code;
    private String desc;

    ResultCode(Integer code, String desc) {
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
