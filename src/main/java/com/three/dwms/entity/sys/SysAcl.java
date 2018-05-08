package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "sys_acl")
public class SysAcl extends BaseEntity implements Serializable {

    private String code;

    private String name;

    private Integer parentId;

    private String icon;

    private String url;

    private Integer type;

    private Integer seq;

}