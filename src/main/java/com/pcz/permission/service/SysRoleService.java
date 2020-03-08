package com.pcz.permission.service;

import com.pcz.permission.model.SysRole;
import com.pcz.permission.param.RoleParam;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysRoleService {
    void save(RoleParam param);

    void update(RoleParam param);

    List<SysRole> getAll();
}
