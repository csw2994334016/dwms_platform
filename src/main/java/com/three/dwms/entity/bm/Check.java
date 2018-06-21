package com.three.dwms.entity.bm;

import com.three.dwms.entity.base.BaseEntity1;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by csw on 2018/6/21.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bm_check")
public class Check extends BaseEntity1 implements Serializable {

    @Column(length = 20, unique = true, nullable = false)
    private String checkNo; //盘点单号

    @Column(length = 20, nullable = false)
    private String checkUser; //盘点人
}
