package com.three.dwms.service.export;

import lombok.*;

import java.util.List;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExportData {

    private String title;

    private List<Record> recordList;

    private String totalMoney;


}
