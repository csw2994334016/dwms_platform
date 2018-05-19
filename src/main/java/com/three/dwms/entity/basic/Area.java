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
@Table(name = "wh_area")
public class Area extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private Integer areaCode; //储区编号

    @Column(length = 20, nullable = false)
    private String areaName; //储区名称

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="zone_id", nullable = false)
    private Zone zone; //仓区

    @Transient
    private Integer areaId;
    public Integer getAreaId() {
        return getId();
    }
}
