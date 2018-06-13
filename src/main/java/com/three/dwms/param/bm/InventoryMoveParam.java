package com.three.dwms.param.bm;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Created by csw on 2018/5/27.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InventoryMoveParam {

    @NotNull(message = "移库汇总id不可以为空")
    private Integer id;

    @NotNull(message = "转移量不可以为空")
    @DecimalMin(value = "0.1", message = "转移量必须大于0")
    private Double moveNumber;

    @NotBlank(message = "目标库房名称不可以为空")
    @Length(min = 1, max = 20, message = "目标库房名称长度需要在1-20个字之间")
    private String whName; //库房名称

    @NotBlank(message = "目标储位代码不可以为空")
    @Length(min = 6, max = 6, message = "目标储位代码长度必须为6位")
    private String locName; //储位代码
}
