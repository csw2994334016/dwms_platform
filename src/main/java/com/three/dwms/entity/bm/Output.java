package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.basic.BanJi;
import com.three.dwms.entity.basic.Project;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/5/24.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_output")
public class Output extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String outputNo; //出库单号，自动生成

    @Column(length = 20, nullable = false)
    private String whName; //仓库名称

    private String proposer; //申请人

    private String approver; //审批人

    @Column(length = 20, nullable = false)
    private String banJiName; //班级名称

    @Column(length = 20, nullable = false)
    private String projectName; //项目名称

    @Column(nullable = false)
    private Integer state; //出库单状态
}
