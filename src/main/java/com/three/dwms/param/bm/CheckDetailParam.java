package com.three.dwms.param.bm;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckDetailParam {

    @NotBlank(message = "仓库编号不可以为空")
    @Length(min = 1, max = 20, message = "仓库编号长度需要在1-20个字之间")
    private String whCode;

    @NotBlank(message = "物料编号不可以为空")
    @Length(min = 1, max = 20, message = "物料编号长度需要在1-20个字之间")
    private String sku; //物料编号

    private Double storeAmount = 0.0; //库存数量

    private Double checkAmount = 0.0; //盘点数量

    private Double difference = 0.0; //差异

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
