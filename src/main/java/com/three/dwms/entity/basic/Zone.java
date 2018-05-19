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
@Table(name = "wh_zone")
public class Zone extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String zoneCode; //仓区编号

    @Column(length = 20, nullable = false)
    private String zoneName; //仓区名称

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="warehouse_id", nullable = false)
    private Warehouse warehouse; //仓库

    @Transient
    private Integer zoneId;
    public Integer getZoneId() {
        return getId();
    }

    @Transient
    private String zoneRemark;
    public String getZoneRemark() {
        return getRemark();
    }
}
