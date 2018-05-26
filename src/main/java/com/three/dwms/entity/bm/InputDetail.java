package com.three.dwms.entity.bm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.three.dwms.entity.base.BaseEntity;
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
@Table(name = "bm_input_detail")
public class InputDetail extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String inputNo; //入库单编号

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    @Column(length = 20, nullable = false)
    private String unitName; //物料单位

    private Double unitPrice; //物料单价

    @Column(nullable = false)
    private Double amount; //物料数量

    private Double totalPrice; //物料总价

    @Column(length = 20)
    private String purchaseDept; //采购部门

    @Column(length = 20)
    private String purchaser; //采购人

    @Column(length = 50)
    private String receiver; //验收人

    @Column(length = 20)
    private String supplierName; //供应商

    @Column(length = 20, nullable = false)
    private Integer state; //物料状态，入库、未入库，转移

    @Column(nullable = false)
    private Date inputDate; //入库日期

    @Column(nullable = false)
    private String whName; //库房名称

    @Column(nullable = false)
    private String whCode; //仓库编号

    @Column(nullable = false)
    private String zoneCode; //仓区编号

    @Column(nullable = false)
    private Integer areaCode; //储区编号

    @Column(nullable = false)
    private Integer locCode; //储位编号

    @Column(nullable = false)
    private String locName; //储位名称，仓区、储区、储位编号拼接而来，6位

}
