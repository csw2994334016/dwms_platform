package com.three.dwms.constant;

/**
 * Created by csw on 2018/5/9.
 * Description:
 */
public enum RoleTypeCode {

    SYSTEM_ADMIN("SYSTEM_ADMIN", "系统管理员"),
    ADMIN("ADMIN", "管理员"), USER("USER", "普通用户");

    private String type;
    private String name;

    RoleTypeCode(String type, String name) {
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
        for (RoleTypeCode roleTypeCode : RoleTypeCode.values()) {
            if (roleTypeCode.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
