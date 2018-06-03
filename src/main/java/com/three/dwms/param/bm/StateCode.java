package com.three.dwms.param.bm;

import lombok.*;

/**
 * Created by csw on 2018/6/3.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StateCode {

    private Integer code;
    private String desc;
}
