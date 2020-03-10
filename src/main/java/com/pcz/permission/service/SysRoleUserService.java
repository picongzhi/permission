package com.pcz.permission.service;

import com.pcz.permission.model.SysUser;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysRoleUserService {
    List<SysUser> getUserListByRoleId(int roleId);

    void changeRoleUsers(int roleId, List<Integer> userIdList);
}
