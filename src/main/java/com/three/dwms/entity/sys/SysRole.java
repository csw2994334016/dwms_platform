package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity implements Serializable {

    private String code; //角色代码：ROLE_ADMIN、ROLE_USER

    private String name; //角色名称

    private Integer type; //角色类型

}