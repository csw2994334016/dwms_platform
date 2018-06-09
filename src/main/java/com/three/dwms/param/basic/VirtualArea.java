package com.three.dwms.param.basic;

import lombok.*;

import java.util.List;

/**
 * Created by csw on 2018/6/8.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VirtualArea {

    private Integer areaId;
    private Integer areaCode;
    private String areaName;

    private Integer usedNum;
    private Integer totalNum;

    private List<VirtualLoc> virtualLocList;
}
