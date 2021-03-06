package com.three.dwms.param.sys;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private String username; //或者叫userId，数据库唯一值，可中英文

    private String password;

    @NotBlank(message = "用户真实姓名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在1-20个字以内")
    private String realName; //用户真实姓名

    @NotNull(message = "必须指定用户的性别")
    @Min(value = 0, message = "用户性别不合法")
    @Max(value = 1, message = "用户性别不合法")
    private Integer sex; //性别，1:男，0：女

    @NotNull(message = "用户角色不可以为空")
    private Integer roleId;

//    @NotBlank(message = "邮箱不允许为空")
    @Length(min = 0, max = 50, message = "邮箱长度需要在50个字符以内")
    private String email;

//    @NotBlank(message = "电话不可以为空")
    @Length(min = 0, max = 13, message = "电话长度需要在13个字以内")
    private String tel;

    private List<String> whCodes; //用户能查看的仓库code：["wh1","wh2","wh3"]

    @NotNull(message = "必须指定用户的状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 2, message = "用户状态不合法")
    private Integer status = StatusCode.NORMAL.getCode(); //1:可用；0：不可用

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark; //备注

    private String keyword; //按关键字查找

}
