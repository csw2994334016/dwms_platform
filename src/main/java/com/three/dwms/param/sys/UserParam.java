package com.three.dwms.param.sys;

import lombok.*;

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

    private String username;

    private String password;

    private Integer status;

    private String remark;
}
