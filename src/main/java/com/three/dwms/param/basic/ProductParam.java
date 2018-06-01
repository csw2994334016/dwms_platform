package com.three.dwms.param.basic;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2018/5/14.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductParam {

    private Integer id;

    @NotBlank(message = "物料名称不可以为空")
    @Length(min = 1, max = 20, message = "物料名称长度需要在1-20个字之间")
    private String skuDesc; //物料名称

    @NotBlank(message = "物料规格/品牌/型号不可以为空")
    @Length(min = 1, max = 20, message = "/品牌/型号长度需要在1-20个字之间")
    private String spec; //物料规格/品牌/型号

    @NotNull(message = "物料分类号不可以为空")
    private Integer categoryId; //物料分类号

    private Integer safeNumber; //物料安全数量

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
