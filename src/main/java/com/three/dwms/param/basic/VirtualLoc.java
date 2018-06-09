package com.three.dwms.param.basic;

import com.three.dwms.entity.bm.Inventory;
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
public class VirtualLoc {

    private Integer locId;
    private Integer locCode;
    private String locName;

    private Boolean useFlag;
    private List<Inventory> inventoryList;
}
