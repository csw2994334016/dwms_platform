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
@Table(name = "sys_user_role")
public class SysUserRole extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer roleId;

}