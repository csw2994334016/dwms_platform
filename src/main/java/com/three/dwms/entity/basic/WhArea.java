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
@Table(name = "wh_area")
public class WhArea extends BaseEntity implements Serializable {

    @Column(length = 20, nullable = false)
    private String areaCode; //储区编号

    @Column(length = 20, nullable = false)
    private String zoneName; //储区名称

    @Column(length = 20, nullable = false)
    private String zoneCode; //仓区编号

    @Column(length = 20, nullable = false)
    private String whCode; //仓库编号
}
