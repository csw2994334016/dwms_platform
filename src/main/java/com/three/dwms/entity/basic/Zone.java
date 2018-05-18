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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="warehouse_id", nullable = false)
    private Warehouse warehouse; //仓库

}
