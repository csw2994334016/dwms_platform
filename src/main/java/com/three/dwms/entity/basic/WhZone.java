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
@Table(name = "wh_zone")
public class WhZone extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String zoneCode; //仓区编号

    @Column(length = 20, nullable = false)
    private String zoneName; //仓区名称

    @Column(length = 20, nullable = false)
    private String whCode; //仓库编号

}
