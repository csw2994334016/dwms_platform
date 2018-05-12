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

    @Column(unique = true, nullable = false)
    private Integer userId; //用户id，用户只有一个角色

    @Column(nullable = false)
    private Integer roleId; //角色id

}