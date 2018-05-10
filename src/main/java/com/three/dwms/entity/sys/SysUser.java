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
    private String username; //用户名

    @Column(length = 50, unique = true, nullable = false)
    private String password; //密码（秘文）

    private Integer sex; //性别

    @Column(length = 50)
    private String email; //邮箱

    @Column(length = 13)
    private String tel; //电话
}