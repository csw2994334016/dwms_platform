package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.basic.Product;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/5/23.
 * Description: 物料汇总表
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_inventory")
public class Inventory extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String whCode;

    @Column(length = 20, nullable = false)
    private String whName;

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20)
    private String spec; //物料规格/品牌/型号

    private Double skuAmount; //物料汇总数量，进出库时对该数量进行更新

}