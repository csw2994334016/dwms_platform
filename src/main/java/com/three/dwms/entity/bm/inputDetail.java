package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_inputDetail")
public class inputDetail extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String inputNo; //入库单编号

    @Column(length = 20, nullable = false)
    private String whCode; //仓库名称

    @Column(length = 20, nullable = false)
    private String location; //物料分类号

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20)
    private String spec; //物料规格/品牌/型号

    @Column(length = 20, nullable = false)
    private String unit; //物料单位

    @Column()
    private Double unitPrice; //物料单价

    @Column(nullable = false)
    private Double amount; //物料数量

    @Column()
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
    private Integer state; //物料状态
}
