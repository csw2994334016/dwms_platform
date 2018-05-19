package com.three.dwms.param.basic;

import com.three.dwms.constant.StateCode;
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
public class AreaParam {

    private Integer id;

//    @NotNull(message = "储区编号不可以为空")
    private Integer areaCode; //储区编号

    @NotBlank(message = "储区名称不可以为空")
    @Length(min = 1, max = 20, message = "储区名称长度需要在1-20个字之间")
    private String areaName; //储区名称

    @NotNull(message = "储区个数不可以为空")
    private Integer areaNum; //自动生成储区的个数

    @NotNull(message = "仓区不可以为空")
    private Integer pzoneId; //仓区id

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StateCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
