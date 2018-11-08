package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by csw on 2018/11/8.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_export_record")
public class ExportRecord extends BaseEntity implements Serializable {

    @Column(length = 9)
    private String exportNo; // 导出编号（E00000001,八位，E开头，不足补0）

    private Double totalMoney; // 总金额

    @Column(length = 20)
    private String purchaser; // 采购人

}
