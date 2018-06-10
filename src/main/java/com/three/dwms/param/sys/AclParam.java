package com.three.dwms.param.sys;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2018/3/23.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AclParam {

    private Integer id;

    @NotBlank(message = "权限点名称不可以为空")
    @Length(min = 2, max = 20, message = "权限点名称长度需要在2-20个字之间")
    private String name;

    @NotNull(message = "必须指定权限父级权限")
    private Integer parentId; //上级权限，一级权限：parentId=0

    private String parentName;

    @NotNull(message = "必须指定权限点的类型")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type; //权限类型：1：菜单，2：按钮，3：其他

    private String icon; //css图标

    @Length(min = 0, max = 100, message = "权限点URL长度需要在4-100个字符之间")
    private String url; //接口地址（可以填正则表达式）：api/sys/users

    private String method; //接口请求的方法

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq; //权限在当前模块下的顺序，由小到大

    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 2, message = "权限点状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "权限点备注长度需要在200个字符以内")
    private String remark;
}
