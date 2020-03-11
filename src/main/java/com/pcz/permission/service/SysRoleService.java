package com.pcz.permission.service;

import com.pcz.permission.model.SysRole;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.RoleParam;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysRoleService {
    void save(RoleParam param);

    void update(RoleParam param);

    List<SysRole> getAll();

    List<SysRole> getRoleListByUserId(int userId);

    List<SysRole> getRoleListByAclId(int aclId);

    List<SysUser> getUserListByRoleList(List<SysRole> sysRoleList);
}
