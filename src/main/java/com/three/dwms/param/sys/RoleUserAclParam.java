package com.three.dwms.param.sys;

import com.google.common.collect.Lists;
import com.three.dwms.constant.RoleTypeCode;
import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Created by csw on 2018/5/8.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleUserAclParam {

    private Integer id;

    @NotBlank(message = "角色名称不可以为空")
    @Length(min = 2, max = 20, message = "角色名称长度需要在2-20个字之间")
    private String name;

//    @NotBlank(message = "角色类型不可以为空")
//    @Length(min = 2, max = 20, message = "角色类型长度需要在2-20个字之间")
    private String type = RoleTypeCode.USER.getType(); //ADMIN、USER两种类型

//    @NotNull(message = "角色状态不可以为空")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "角色备注长度需要在200个字符以内")
    private String remark;

    @Builder.Default
    private List<Integer> userIds = Lists.newArrayList();
    @Builder.Default
    private List<Integer> aclIds = Lists.newArrayList();

    public List<Integer> getUserIds() {
        if (userIds == null) {
            return Lists.newArrayList();
        }
        return userIds;
    }

    public List<Integer> getAclIds() {
        if (aclIds == null) {
            return Lists.newArrayList();
        }
        return aclIds;
    }
}
