package com.three.dwms.entity.basic;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.three.dwms.entity.base.BaseEntity;
import com.three.dwms.entity.base.BaseEntity1;
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
@Table(name = "wh_warehouse")
public class Warehouse extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String whCode; //仓库编号

    @Column(length = 20, unique = true, nullable = false)
    private String whName; //仓库名称

    @Transient
    private Integer whId;
    public Integer getWhId() {
        return getId();
    }

    @Transient
    private String whRemark;
    public String getWhRemark() {
        return getRemark();
    }
}
