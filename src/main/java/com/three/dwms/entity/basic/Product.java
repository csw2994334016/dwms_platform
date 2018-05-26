package com.three.dwms.entity.basic;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "basic_product")
public class Product extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, unique = true, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    private Integer safeNumber; //物料安全数量

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="category_id", nullable = false)
    private Category category; //物料分类号

}
