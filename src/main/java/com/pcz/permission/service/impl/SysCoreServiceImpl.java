package com.pcz.permission.service.impl;

import com.google.common.collect.Lists;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysAclMapper;
import com.pcz.permission.dao.SysRoleAclMapper;
import com.pcz.permission.dao.SysRoleUserMapper;
import com.pcz.permission.model.SysAcl;
import com.pcz.permission.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author picongzhi
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {
    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getByIdList(aclIdList);
    }

    @Override
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getByIdList(userAclIdList);
    }

    private boolean isSuperAdmin() {
        return true;
    }
}
