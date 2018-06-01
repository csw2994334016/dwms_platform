package com.three.dwms.param.bm;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OutputParam {

    private Integer id;

    @NotBlank(message = "仓库名称不可以为空")
    @Length(min = 1, max = 20, message = "仓库名称长度需要在1-20个字之间")
    private String whName; //仓库名称

    @NotBlank(message = "审批人不可以为空")
    @Length(min = 1, max = 20, message = "审批人长度需要在1-20个字之间")
    private String approver; //审批人

    @NotBlank(message = "班级名称不可以为空")
    @Length(min = 1, max = 20, message = "班级名称长度需要在1-20个字之间")
    private String banJiName; //班级名称

    @NotBlank(message = "项目名称不可以为空")
    @Length(min = 1, max = 20, message = "项目名称长度需要在1-20个字之间")
    private String projectName; //项目名称

    private Integer state; //出库单状态

    @Min(value = 0, message = "状态不合法")
    @Max(value = 2, message = "状态不合法")
    private Integer status = StatusCode.NORMAL.getCode();

    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;

    private List<OutputDetailParam> outputDetailParamList;//领料详情

}
