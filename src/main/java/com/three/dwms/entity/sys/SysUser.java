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
@Table(name = "sys_user")
public class SysUser extends BaseEntity implements Serializable{

    @Column(length = 20, unique = true, nullable = false)
    private String username; //用户名/用户ID

    @Column(length = 50, unique = true, nullable = false)
    private String password; //密码（秘文）

    @Column(length = 20, unique = true, nullable = false)
    private String realName; //真实姓名

    @Column(length = 1)
    private Integer sex; //性别

    @Column(length = 50, unique = true)
    private String email; //邮箱

    @Column(length = 13, unique = true)
    private String tel; //电话
}