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
@Table(name = "basic_banJi")
public class BanJi extends BaseEntity implements Serializable {

    @Column(length = 7, unique = true, nullable = false)
    private String banJiCode; //班级编号

    @Column(length = 20, unique = true, nullable = false)
    private String banJiName; //班级名称
}
