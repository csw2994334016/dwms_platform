package com.three.dwms.constant;

/**
 * Created by csw on 2017/12/9.
 * Description:
 */
public enum ResultCode {

    SUCCESS("0000", "操作成功"), NO_LOGIN("0001", "登录失效"),
    FAIL("0002", "操作失败");

    private String code;
    private String desc;

    ResultCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
