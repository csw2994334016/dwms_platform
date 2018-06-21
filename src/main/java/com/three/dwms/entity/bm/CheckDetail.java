package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity1;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_check_detail")
public class CheckDetail extends BaseEntity1 implements Serializable {

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "check_id", nullable = false)
    private Check check;

    @Column(length = 20, nullable = false)
    private String whCode; //仓库编号

    @Column(length = 20, nullable = false)
    private String whName; //仓库名称

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    @Column(nullable = false)
    private Double storeAmount; //库存数量

    @Column(nullable = false)
    private Double checkAmount; //盘点数量

    @Column(nullable = false)
    private Double difference; //差异

}
