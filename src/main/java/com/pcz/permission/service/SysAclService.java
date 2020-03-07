package com.pcz.permission.service;

import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.beans.PageResult;
import com.pcz.permission.model.SysAcl;
import com.pcz.permission.param.AclParam;

/**
 * @author picongzhi
 */
public interface SysAclService {
    void save(AclParam param);

    void update(AclParam param);

    PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery query);
}
