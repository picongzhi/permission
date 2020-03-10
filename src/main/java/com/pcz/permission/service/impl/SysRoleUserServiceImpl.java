package com.pcz.permission.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysRoleUserMapper;
import com.pcz.permission.dao.SysUserMapper;
import com.pcz.permission.model.SysRoleUser;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.service.SysRoleUserService;
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
public class SysRoleUserServiceImpl implements SysRoleUserService {
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getUserListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByIdIdList(userIdList);
    }

    @Override
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }

        updateRoleAcls(roleId, userIdList);
    }

    @Transactional
    void updateRoleAcls(Integer roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        List<SysRoleUser> sysRoleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser sysRoleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();
            sysRoleUserList.add(sysRoleUser);
        }

        sysRoleUserMapper.batchInsert(sysRoleUserList);
    }
}
