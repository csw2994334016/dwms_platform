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
@Table(name = "wh_loc")
public class Loc extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private Integer locCode; //储位编号

    @Column(length = 20, nullable = false)
    private String locName; //储位名称

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_id")
    private Area area; //储区

}
