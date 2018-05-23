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

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="product_id", nullable = false)
    private Product product; //物料信息

    private Double skuAmount; //物料汇总数量，进出库时对该数量进行更新

}
