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
@Table(name = "sys_role_acl")
public class SysRoleAcl extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer aclId;

}