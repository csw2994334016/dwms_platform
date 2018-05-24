package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Table(name = "bm_input")
public class Input extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String inputNo; //入库单号，自动生成

    @Column(length = 20, nullable = false)
    private String whCode; //仓库编号

    @Column(nullable = false)
    private Integer state; //入库单状态

}
