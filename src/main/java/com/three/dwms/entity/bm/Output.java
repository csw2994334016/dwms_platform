package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.basic.BanJi;
import com.three.dwms.entity.basic.Project;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    private String whCode; //仓库编号

    private String proposer; //申请人

    private String approver; //审批人

    private BanJi banJi; //班级

    private Project project; //项目

    @Column(nullable = false)
    private Integer state; //出库单状态
}
