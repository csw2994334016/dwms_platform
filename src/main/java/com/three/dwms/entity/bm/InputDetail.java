package com.three.dwms.entity.bm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_inputDetail")
public class InputDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id; //主键

    @Column(length = 20, nullable = false)
    private String inputNo; //入库单编号

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20)
    private String spec; //物料规格/品牌/型号

    @Column(length = 20, nullable = false)
    private String unit; //物料单位

    private Double unitPrice; //物料单价

    @Column(nullable = false)
    private Double amount; //物料数量

    private Double totalPrice; //物料总价

    @Column(length = 20)
    private String purchaseDept; //采购部门

    @Column(length = 20)
    private String purchaser; //采购人

    @Column(length = 20)
    private String receiver; //验收人

    @Column(length = 20)
    private String supplierName; //供应商

    @Column(length = 20, nullable = false)
    private Integer state; //物料状态，入库、未入库

    private String whCode; //仓库编号

    private String zoneCode; //仓区编号

    private Integer areaCode; //储区编号

    private Integer locCode; //储位编号

    private String locName; //储位名称，仓库、仓区、储区、储位编号拼接而来

    private String whName; //库房名称

    private Date inputDate; //入库日期

    private Integer status; //状态：1：可用，0：禁用，2：删除

    private String remark; //备注

    private String creator; //创建者

    private Date createTime; //创建时间

    private String operator; //最后一次操作者

    private Date operateTime; //最后一次操作时间

    @JsonIgnore
    private String operateIp; //最后一次操作的ip地址

}
