package com.three.dwms.param.bm;

import com.three.dwms.constant.StateCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by csw on 2018/5/25.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InputDetailParam {

    private Integer id;

    @NotBlank(message = "物料名称不可以为空")
    @Length(min = 1, max = 20, message = "物料名称长度需要在1-20个字之间")
    private String skuDesc; //物料名称

    @NotBlank(message = "物料规格/品牌/型号不可以为空")
    @Length(min = 1, max = 20, message = "物料规格/品牌/型号长度需要在1-20个字之间")
    private String spec; //物料规格/品牌/型号

    @NotBlank(message = "单位名称不可以为空")
    @Length(min = 1, max = 20, message = "单位名称长度需要在1-20个字之间")
    private String unitName; //物料单位

    private Double unitPrice; //物料单价

    private Double amount; //物料数量

    private Double totalPrice; //物料总价

    private String purchaseDept; //采购部门

    private String purchaser; //采购人

    private String receiver; //验收人

    private String supplierName; //供应商

    private String whName; //库房名称

    private String locName; //储位代码

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StateCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
