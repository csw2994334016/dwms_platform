package com.three.dwms.param.bm;


import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OutputDetailParam {

    private Integer id;

    @NotBlank(message = "物料编号不可以为空")
    @Length(min = 1, max = 20, message = "物料编号长度需要在1-20个字之间")
    private String sku; //物料编号

    @NotBlank(message = "物料名称不可以为空")
    @Length(min = 1, max = 20, message = "物料名称长度需要在1-20个字之间")
    private String skuDesc; //物料名称

    @NotBlank(message = "物料规格/品牌/型号不可以为空")
    @Length(min = 1, max = 20, message = "物料规格/品牌/型号长度需要在1-20个字之间")
    private String spec; //物料规格/品牌/型号

    @NotNull(message = "出库数量不可以为空")
    @DecimalMin(value = "1.0", message = "出库数量必须大于1")
    private Double outNumber; //出库数量

    private Double returnNumber = 0.0; //归还数量

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
