package com.three.dwms.param.sys;

import com.three.dwms.entity.sys.SysAcl;
import com.three.dwms.entity.sys.SysRole;
import com.three.dwms.entity.sys.SysUser;
import lombok.*;

import java.util.List;


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
public class UserRoleAcl {

    private SysUser sysUser;

    private List<SysAcl> sysAclList;

}
