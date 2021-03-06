package com.three.dwms.param.sys;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private Integer id;

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    @NotBlank(message = "旧密码不可以为空")
    @Length(min = 1, max = 50, message = "旧密码长度需要在20个字以内")
    private String oldPassword;

    @NotBlank(message = "密码不可以为空")
    @Length(min = 1, max = 50, message = "密码长度需要在20个字以内")
    private String password;
}
