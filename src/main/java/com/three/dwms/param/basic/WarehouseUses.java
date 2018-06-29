package com.three.dwms.param.basic;

import lombok.*;

/**
 * Created by csw on 2018/6/29.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WarehouseUses {

    private String whName; //仓库名称
    private Double usedRatio; //使用比率
    private Double unUsedRatio; //未使用比率
}
