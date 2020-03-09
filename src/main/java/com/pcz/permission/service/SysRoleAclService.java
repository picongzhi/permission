package com.pcz.permission.service;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysRoleAclService {
    void changeRoleAcls(Integer roleId, List<Integer> aclIdList);
}
