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
@Table(name = "wh_warehouse")
public class Warehouse extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String whCode; //仓库编号

    @Column(length = 20, unique = true, nullable = false)
    private String whName; //仓库名称

}
