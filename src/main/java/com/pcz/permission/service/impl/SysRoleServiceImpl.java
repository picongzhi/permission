package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysRoleAclMapper;
import com.pcz.permission.dao.SysRoleMapper;
import com.pcz.permission.dao.SysRoleUserMapper;
import com.pcz.permission.dao.SysUserMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysRole;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.RoleParam;
import com.pcz.permission.service.SysRoleService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author picongzhi
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole sysRole = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());

        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    @Override
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roldIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roldIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roldIdList);
    }

    @Override
    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysUser> getUserListByRoleList(List<SysRole> sysRoleList) {
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return Lists.newArrayList();
        }

        List<Integer> roleIdList = sysRoleList.stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByIdList(userIdList);
    }
}
