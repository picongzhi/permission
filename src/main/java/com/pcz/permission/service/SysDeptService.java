package com.pcz.permission.service;

import com.pcz.permission.param.DeptParam;

/**
 * @author picongzhi
 */
public interface SysDeptService {
    void save(DeptParam deptParam);

    void update(DeptParam deptParam);

    void delete(int deptId);
}
