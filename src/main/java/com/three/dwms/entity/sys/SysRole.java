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
    private String type; //角色类型，1：超级管理员角色ROLE_ADMIN，2：其他ROLE_USER

}