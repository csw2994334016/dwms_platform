package com.three.dwms.param.sys;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Created by csw on 2018/5/12.
 * Description:
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AclMenu {

    @JsonProperty("F_ModuleId")
    private String id;

    @JsonProperty("F_ParentId")
    private String parentId;

    @JsonProperty("F_FullName")
    private String name;

    @JsonProperty("F_Icon")
    private String icon;

    @JsonProperty("F_UrlAddress")
    private String url;

}
