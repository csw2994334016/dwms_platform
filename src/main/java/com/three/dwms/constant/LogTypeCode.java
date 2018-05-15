package com.three.dwms.constant;

/**
 * Created by csw on 2018/3/20.
 * Description:
 */
public enum  LogTypeCode {

    TYPE_USER(1, "用户"),
    TYPE_ROLE(2, "角色"),
    TYPE_ACL(3, "权限"),
    TYPE_USER_ROLE(4, "用户角色"),
    TYPE_USER_ACL(5, "用户权限"),
    TYPE_CATEGORY(6, "物料分类"), TYPE_PRODUCT(5, "物料信息");

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
