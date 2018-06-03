package com.three.dwms.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by csw on 2018/5/8.
 * Description:
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id; //主键

    @Column(nullable = false)
    private Integer status; //状态：1：可用，0：禁用，2：删除

    private String remark; //备注

    private String creator; //创建者

    private Date createTime; //创建时间

    private String operator; //最后一次操作者

    private Date operateTime; //最后一次操作时间

    private String operateIp; //最后一次操作的ip地址

}
