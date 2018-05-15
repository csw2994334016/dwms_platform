package com.three.dwms.entity.sys;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by csw on 2018/5/11.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_log")
public class SysLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer type;

    private Integer targetId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String oldValue;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String newValue;

    private String operator; //最后一次操作者

    private Date operateTime; //最后一次操作时间

    private String operateIp; //最后一次操作的ip地址
}
