package com.three.dwms.entity.sys;

import com.three.dwms.entity.base.BaseEntity1;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by csw on 2018/6/29.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_notice")
public class SysNotice extends BaseEntity1 implements Serializable {

    @Column(nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String content;

    private String noticeType;
}
