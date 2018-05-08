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

    @Column(length = 20)
    private String username;

    private String password;

    private Integer sex;

    private String email;

    @Column(length = 13)
    private String tel;
}