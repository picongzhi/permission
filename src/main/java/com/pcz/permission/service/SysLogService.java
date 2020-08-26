package com.pcz.permission.service;

import com.pcz.permission.model.*;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysLogService {
    void saveDeptLog(SysDept before, SysDept after);

    void saveUserLog(SysUser before, SysUser after);

    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    void saveAclLog(SysAcl before, SysAcl after);

    void saveRoleLog(SysRole before, SysRole after);

    void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after);

    void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after);
}
