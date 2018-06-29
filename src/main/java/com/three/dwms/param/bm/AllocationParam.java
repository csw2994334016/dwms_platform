package com.three.dwms.param.bm;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2018/6/1.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AllocationParam {

    @NotNull(message = "拣货出库/退还时，库房物料汇总信息id不可以为空")
    private Integer id;

    @NotBlank(message = "出库单号不可以为空")
    @Length(min = 1, max = 20, message = "出库单号长度需要在1-20个字之间")
    private String outputNo;

    @NotNull(message = "实际领用量不可以为空")
    private Double allocateAmount = 0.0; //实际出库数量

    private Double returnNumber = 0.0; //退还数量

    private String sku;
    private Double allReturnNumber = 0.0;

}
