package com.pcz.permission.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author picongzhi
 */
@Getter
@Setter
@ToString
public class RoleAclParam {
    private Integer roleId;
    private String aclIds;
}
