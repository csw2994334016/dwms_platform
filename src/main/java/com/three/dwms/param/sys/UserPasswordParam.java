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
public class UserPasswordParam {

    @NotBlank(message = "旧密码不可以为空")
    @Length(min = 1, max = 50, message = "旧密码长度需要在50个字以内")
    private String oldPassword;

    @NotBlank(message = "新密码不可以为空")
    @Length(min = 1, max = 50, message = "新密码长度需要在50个字以内")
    private String newPassword;

    @NotBlank(message = "确认密码不可以为空")
    @Length(min = 1, max = 50, message = "确认密码长度需要在50个字以内")
    private String suePassword;
}
