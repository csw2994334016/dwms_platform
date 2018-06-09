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
public class VirtualZone {

    private Integer zoneId;
    private String zoneName;

    private List<VirtualArea> virtualAreaList;
}
