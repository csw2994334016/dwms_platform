package com.three.dwms.entity.bm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by csw on 2018/6/23.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_record")
public class Record extends BaseEntity implements Serializable {

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordDate; //记录日期

    @Column(length = 20, nullable = false)
    private String whName; //仓库名称

    @Column(length = 20, nullable = false)
    private String sku; //物料编号

    @Column(length = 20, nullable = false)
    private String skuDesc; //物料名称

    @Column(length = 20, nullable = false)
    private String spec; //物料规格/品牌/型号

    @Column(nullable = false)
    private Double recordAmount; //缺失数量

    @Column(nullable = false)
    private Integer recordType; //缺失类型，报废、损耗、丢失、其他

    @Column(nullable = false)
    private String person; //责任人

    private String reason; //原因

}
