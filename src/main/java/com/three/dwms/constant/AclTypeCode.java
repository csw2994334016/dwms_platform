package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/10.
 * Description:
 */
public enum  AclTypeCode {

    TYPE_MENU(1, "菜单"), TYPE_BUTTON(2, "按钮");

    private int code;
    private String desc;

    AclTypeCode(int code, String desc) {
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
