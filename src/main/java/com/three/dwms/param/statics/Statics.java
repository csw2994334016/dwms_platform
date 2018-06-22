package com.three.dwms.param.statics;

import lombok.*;

import java.util.List;

/**
 * Created by csw on 2018/6/22.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Statics {

    private List<String> labelList;

    private List<Double> dataList;
}
