package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public enum DefaultRole {

    ROLE_ADMIN("ROLE_ADMIN", "超级管理员"), ROLE_USER("ROLE_USER", "普通用户");

    private String type;
    private String name;

    DefaultRole(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static boolean exist(String type) {
        for (DefaultRole defaultRole : DefaultRole.values()) {
            if (defaultRole.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
