package com.pcz.permission.service;

import com.pcz.permission.param.AclModuleParam;

public interface SysAclModuleService {
    void save(AclModuleParam param);

    void update(AclModuleParam param);

    void delete(int aclModuleId);
}
