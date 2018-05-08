package com.three.dwms.entity.base;

import com.three.dwms.constant.StateCode;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by csw on 2018/5/8.
 * Description:
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    //状态，1：可用，0：禁用，2：删除
    private Integer status;

    private String remark;

    private String creator;

    private Date createTime;

    private String operator;

    private Date operateTime;

    private String operateIp;


}
