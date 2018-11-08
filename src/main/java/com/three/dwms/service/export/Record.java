package com.three.dwms.service.export;

import lombok.*;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Record {

    private String whName; //库房名称

    private String skuDesc; //物料名称

    private String spec; //物料规格/品牌/型号

    private String unitName; //物料单位

    private String unitPrice; //物料单价

    private String amount; //物料数量，入库数量

    private String totalPrice; //物料总价

    private String purchaser; //采购人

    private String createTime; //导出日期
}
