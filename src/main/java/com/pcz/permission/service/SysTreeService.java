package com.pcz.permission.service;

import com.pcz.permission.dto.AclModuleLevelDto;
import com.pcz.permission.dto.DeptLevelDto;

import java.util.List;

/**
 * @author picongzhi
 */
public interface SysTreeService {
    List<DeptLevelDto> deptTree();

    List<AclModuleLevelDto> aclModuleTree();
}
