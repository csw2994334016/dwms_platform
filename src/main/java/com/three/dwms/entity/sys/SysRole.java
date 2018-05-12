package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity implements Serializable {

    @Column(unique = true, nullable = false)
    private String name; //角色名称

    @Column(nullable = false)
    private String type; //角色类型，1：系统管理员角色SYSTEM_ADMIN、管理员ADMIN、普通用户USER

}