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
@Table(name = "basic_supplier")
public class Supplier extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String supplierCode; //供应商编号

    @Column(length = 20, unique = true, nullable = false)
    private String supplierName; //供应商名称
}
