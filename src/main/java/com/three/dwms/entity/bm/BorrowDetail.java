package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/5/24.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_borrow_detail")
public class BorrowDetail extends BaseEntity implements Serializable {

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "borrow_id", nullable = false)
    private Borrow borrow; //借出单号

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    @Column(nullable = false)
    private Double borrowNumber; //借出数量

    @Column(nullable = false)
    private Double notReturnNumber; //未归还数量，

    @Column(nullable = false)
    private Double returnNumber; //归还数量
}
