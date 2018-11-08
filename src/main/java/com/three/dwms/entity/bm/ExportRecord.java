package com.three.dwms.entity.bm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
@Entity
@Table(name = "bm_export_record")
public class ExportRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id; //主键

    @Column(length = 9)
    private String exportNo; // 导出编号（E00000001,八位，E开头，不足补0）

    private Double totalMoney; // 总金额

    @Column(length = 20)
    private String purchaser; // 采购人

    @Column(nullable = false)
    private Integer status; //状态：1：可用，0：禁用，2：删除

    private String remark; //备注

    @JsonIgnore
    private String creator; //创建者

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; //创建时间

    @JsonIgnore
    private String operator; //最后一次操作者

    @JsonIgnore
    private Date operateTime; //最后一次操作时间

    @JsonIgnore
    private String operateIp; //最后一次操作的ip地址

}
