package com.three.dwms.param.basic;

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
public class WarehouseTree {

    private Integer id;
    private Integer type;
    private String text;
    private List<WarehouseTree> nodes;
}
