package com.three.dwms.param.sys;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

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
public class AclTree {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer seq;

    @Builder.Default
    private List<AclTree> children = Lists.newArrayList();
}
