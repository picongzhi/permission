package com.pcz.permission.service.impl;

import com.google.common.collect.Lists;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysAclMapper;
import com.pcz.permission.dao.SysRoleAclMapper;
import com.pcz.permission.dao.SysRoleUserMapper;
import com.pcz.permission.model.SysAcl;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        SysUser sysUser = RequestHolder.getCurrentUser();
        return sysUser.getMail().contains("admin");
    }

    @Override
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }

        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclList();
        Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

        // 规则：只要有一个权限点有权限，就认为有访问权限
        boolean hasValidAcl = false;
        for (SysAcl sysAcl : aclList) {
            if (sysAcl == null || sysAcl.getStatus() != 1) {
                continue;
            }

            hasValidAcl = true;
            if (userAclIdSet.contains(sysAcl.getId())) {
                return true;
            }
        }

        return !hasValidAcl;
    }
}
