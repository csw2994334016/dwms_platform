package com.three.dwms.param.sys;

import com.three.dwms.entity.sys.SysUser;
import lombok.*;


/**
 * Created by csw on 2018/5/9.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SessionUser {

    private SysUser sysUser;

}
