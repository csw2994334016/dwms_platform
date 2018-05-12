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
@Table(name = "basic_category")
public class Category extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String categoryName; //分类名称，物料、耗材等分类名称
}
