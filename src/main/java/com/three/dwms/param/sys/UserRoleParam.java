package com.three.dwms.param.sys;

import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * Created by csw on 2018/5/12.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRoleParam {

    @NotNull(message = "用户Id不可以为空")
    private Integer userId;

    @NotNull(message = "角色Id不可以为空")
    private Integer roleId;
}
