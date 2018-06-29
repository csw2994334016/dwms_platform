package com.three.dwms.param.statics;

import lombok.*;

/**
 * Created by csw on 2018/6/22.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StaticsParam {

    private String sku; //物料编号

    private String purchaseDept; //采购部门

    private String banJiName;

    private String projectName;

    private String year;

    private String month;
}
