package com.pcz.permission.service;

import com.pcz.permission.model.SysAcl;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysCoreService {
    List<SysAcl> getCurrentUserAclList();

    List<SysAcl> getRoleAclList(int roleId);

    List<SysAcl> getUserAclList(int userId);

    boolean hasUrlAcl(String url);

    List<SysAcl> getCurrentUserAclListFromCache();
}
