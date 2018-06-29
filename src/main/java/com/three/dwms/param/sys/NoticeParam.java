package com.three.dwms.param.sys;

import com.three.dwms.constant.StatusCode;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by csw on 2018/5/6.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeParam {

    private Integer id;

    @NotBlank(message = "标题不可以为空")
    @Length(min = 1, max = 50, message = "标题长度需要在50个字以内")
    private String title;

    @NotBlank(message = "内容不可以为空")
    private String content;

    private String noticeType;

    private Integer status = StatusCode.NORMAL.getCode(); //1:可用；0：不可用

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark; //备注

}
