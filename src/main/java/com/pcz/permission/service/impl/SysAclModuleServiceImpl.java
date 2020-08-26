package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysAclMapper;
import com.pcz.permission.dao.SysAclModuleMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysAclModule;
import com.pcz.permission.param.AclModuleParam;
import com.pcz.permission.service.SysAclModuleService;
import com.pcz.permission.service.SysLogService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.IpUtil;
import com.pcz.permission.util.LevelUtil;
import com.pcz.permission.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author picongzhi
 */
@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule sysAclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperateTime(new Date());

        sysAclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null, sysAclModule);
    }

    @Override
    public void update(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");

        SysAclModule after = SysAclModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);
    }

    @Transactional
    void updateWithChild(SysAclModule before, SysAclModule after) {
        String oldLevelPrefix = LevelUtil.calculateLevel(before.getLevel(), before.getId());
        String newLevelPrefix = after.getLevel();

        if (!oldLevelPrefix.equals(newLevelPrefix)) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = LevelUtil.calculateLevel(newLevelPrefix, aclModule.getParentId());
                        aclModule.setLevel(level);
                    }
                }

                for (SysAclModule sysAclModule : aclModuleList) {
                    sysAclModuleMapper.updateByPrimaryKeySelective(sysAclModule);
                }
//                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }

        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null) {
            return null;
        }

        return sysAclModule.getLevel();
    }

    @Override
    public void delete(int aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(sysAclModule, "待删除的权限模块不存在，无法删除");

        if (sysAclModuleMapper.countByParentId(aclModuleId) > 0) {
            throw new ParamException("待删除的权限模块存在子模块，无法删除");
        }

        if (sysAclMapper.countByAclModuleId(aclModuleId) > 0) {
            throw new ParamException("待删除的权限模块存在权限点，无法删除");
        }

        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }
}
