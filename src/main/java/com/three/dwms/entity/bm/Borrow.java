package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity1;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

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
@Entity
@Table(name = "bm_borrow")
public class Borrow extends BaseEntity1 implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String borrowNo; //借出单号

    @Column(length = 20, nullable = false)
    private String whCode;

    @Column(length = 20, nullable = false)
    private String whName;

    @Column(length = 20, nullable = false)
    private String borrowUser; //借出人

    @Column(length = 20, nullable = false)
    private String approveUser; //审批人

    @Column(length = 20, nullable = false)
    private String banJiName; //班级名称

    @Column(length = 20, nullable = false)
    private String projectName; //项目名称

    @Column(nullable = false)
    private Date borrowDate; //借出时间

    @Column(nullable = false)
    private Date returnDate; //归还时间

    @Column(nullable = false)
    private Integer state;
}
