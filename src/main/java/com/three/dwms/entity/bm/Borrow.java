package com.three.dwms.entity.bm;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String proposer; //借出人

    @Column(length = 20, nullable = false)
    private String approver; //审批人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date borrowDate; //借出时间

    private String reason; //借出原因

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date returnDate; //归还日期，预计归还时间，借出申请时填写

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualReturnDate; //归还时间，实际归还时间，出库时系统自动生成

    @Column(nullable = false)
    private Integer state;
}
