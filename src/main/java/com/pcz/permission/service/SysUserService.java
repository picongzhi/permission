package com.pcz.permission.service;

import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.UserParam;

/**
 * @author picongzhi
 */
public interface SysUserService {
    void save(UserParam param);

    void update(UserParam param);

    SysUser findByKeyword(String keyword);
}
