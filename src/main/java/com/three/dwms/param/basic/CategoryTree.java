package com.three.dwms.param.basic;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

/**
 * Created by csw on 2018/5/16.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryTree {

    private Integer id;
    private String text;
    private List<CategoryTree> nodes;
}
