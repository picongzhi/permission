package com.pcz.permission.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysRoleAclMapper;
import com.pcz.permission.model.SysRoleAcl;
import com.pcz.permission.service.SysRoleAclService;
import com.pcz.permission.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author picongzhi
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }

        updateRoleAcls(roleId, aclIdList);
    }

    @Transactional
    void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

        List<SysRoleAcl> sysRoleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();
            sysRoleAclList.add(sysRoleAcl);
        }

        sysRoleAclMapper.batchInsert(sysRoleAclList);
    }
}
