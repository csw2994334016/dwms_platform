package com.three.dwms.param.basic;

import com.three.dwms.constant.StateCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


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
public class SupplierParam {

    private Integer id;

    @NotBlank(message = "项目编号不可以为空")
    @Length(min = 1, max = 20, message = "项目编号长度需要在1-20个字之间")
    private String supplierCode; //供应商编号

    @NotBlank(message = "项目名称不可以为空")
    @Length(min = 1, max = 20, message = "项目名称长度需要在1-20个字之间")
    private String supplierName; //供应商名称

    @Length(min = 0, max = 13, message = "电话长度需要在13个字以内")
    private String supplierTel; //供应商电话

    private String supplierAddress; //供应商地址

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StateCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}
