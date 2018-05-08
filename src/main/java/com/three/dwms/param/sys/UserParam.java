package com.three.dwms.param.sys;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
public class UserParam {

    private Integer id;

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    @NotNull(message = "必须指定用户的性别")
    @Min(value = 0, message = "用户性别不合法")
    @Max(value = 1, message = "用户性别不合法")
    private Integer sex;

    @NotBlank(message = "邮箱不允许为空")
    @Length(min = 5, max = 50, message = "邮箱长度需要在50个字符以内")
    private String email;

    @NotBlank(message = "电话不可以为空")
    @Length(min = 1, max = 13, message = "电话长度需要在13个字以内")
    private String tel;

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status;

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark;

    private String createId;

    private Date createTime;

    private String operatorId;

    private Date operateTime;

    private String operateIp;
}
