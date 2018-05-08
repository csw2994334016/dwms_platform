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

    private Integer status;

    private String remark;

    private Integer createId;

    private Date createTime;

    private Integer operatorId;

    private Date operateTime;

    private String operateIp;


}
