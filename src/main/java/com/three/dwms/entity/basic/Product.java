package com.three.dwms.entity.basic;

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
@Table(name = "product")
public class Product extends BaseEntity implements Serializable{
    @Column(length = 20, unique = true, nullable = false)
    private String sku; //物料编号

    @Column(length = 20,unique = true, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20,  nullable = false)
    private String categoryId; //物料分类号

    @Column(length = 20)
    private String spec; //物料规格/品牌/型号

}
