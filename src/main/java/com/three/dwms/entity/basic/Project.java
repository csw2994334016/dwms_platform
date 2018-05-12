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
@Table(name = "basic_project")
public class Project extends BaseEntity implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String projectCode; //项目编号

    @Column(length = 20, unique = true, nullable = false)
    private String projectName; //项目名称

}
