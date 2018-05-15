package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_acl")
public class SysAcl extends BaseEntity implements Serializable {

    @Column(unique = true, nullable = false)
    private String name; //权限名称

    @Column(nullable = false)
    private Integer parentId; //上级权限，可以分模块管理权限；一级权限parentId=0;

    private Integer type; //权限类型：1：菜单，2：按钮，3：其他

    private String icon; //css图标

    private String url; //接口地址（可以填正则表达式）：api/sys/users

    private Integer seq; //权限在当前模块下的顺序，由小到大

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="sysAcl")
    private SysAcl sysAcl; //上级权限，可以分模块管理权限；一级权限parentId=0;

    @Transient
    private Boolean open = true;
    @Transient
    private Boolean checked = false;
}