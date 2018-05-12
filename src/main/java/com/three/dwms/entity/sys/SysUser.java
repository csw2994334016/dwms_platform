package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String username; //用户名/用户ID

    @Column(length = 50, nullable = false)
    private String password; //密码（秘文）

    @Column(length = 20, nullable = false)
    private String realName; //真实姓名

    private Integer sex; //性别，1:男，0：女

    @Column(length = 50, unique = true)
    private String email; //邮箱

    @Column(length = 13, unique = true)
    private String tel; //电话

    @Column(unique = true, nullable = false)
    private Integer roleId; //角色Id，唯一值

    @Transient
    private SysRole sysRole;
    @Transient
    private List<SysAcl> sysAclList;
}