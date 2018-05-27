package com.three.dwms.param.bm;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
public class InventoryParam {

    private Integer id;

    @NotBlank(message = "库房名称不可以为空")
    @Length(min = 1, max = 20, message = "库房名称长度需要在1-20个字之间")
    private String whName;
}
