package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "sys_acl")
public class SysAcl extends BaseEntity implements Serializable {

    @Column(unique = true, nullable = false)
    private String code; //英文权限码

    @Column(unique = true, nullable = false)
    private String name; //权限名称

    @Column(unique = true, nullable = false)
    private Integer parentId; //上级权限，可以分模块

    private String icon; //css图标

    private String url; //接口地址（可以填正则表达式）：api/sys/users

    private Integer type; //权限类型：1：菜单，2：按钮，3：其他

    private Integer seq; //权限在当前模块下的顺序，由小到大

}