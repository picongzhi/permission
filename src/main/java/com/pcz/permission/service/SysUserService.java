package com.pcz.permission.service;

import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.beans.PageResult;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.UserParam;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysUserService {
    void save(UserParam param);

    void update(UserParam param);

    SysUser findByKeyword(String keyword);

    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

    List<SysUser> getAll();
}
