package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.basic.Product;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/5/23.
 * Description: 物料盘点表
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_stock_count")
public class StockCount extends BaseEntity implements Serializable {

    private String stockCode; //盘点单号，自动生成，规则：SC000001

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    private Double recordAmount; //系统记录的数量

    private Double realAmount; //人工盘点数量

    private String stockCounter; //盘点人员

}
