package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysDeptMapper;
import com.pcz.permission.dao.SysUserMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysDept;
import com.pcz.permission.param.DeptParam;
import com.pcz.permission.service.SysDeptService;
import com.pcz.permission.service.SysLogService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.IpUtil;
import com.pcz.permission.util.LevelUtil;
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
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept sysDept = SysDept.builder()
                .name(deptParam.getName())
                .parentId(deptParam.getParentId())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark())
                .build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());

        sysDeptMapper.insertSelective(sysDept);
        sysLogService.saveDeptLog(null, sysDept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }

        return sysDept.getLevel();
    }

    @Override
    public void update(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        SysDept after = SysDept.builder()
                .id(deptParam.getId())
                .name(deptParam.getName())
                .parentId(deptParam.getParentId())
                .seq(deptParam.getSeq())
                .remark(deptParam.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    @Transactional
    void updateWithChild(SysDept before, SysDept after) {
        String oldLevelPrefix = LevelUtil.calculateLevel(before.getLevel(), before.getId());
        String newLevelPrefix = after.getLevel();

        if (!oldLevelPrefix.equals(newLevelPrefix)) {
            List<SysDept> sysDeptList = sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
            if (CollectionUtils.isNotEmpty(sysDeptList)) {
                for (SysDept sysDept : sysDeptList) {
                    String level = sysDept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = LevelUtil.calculateLevel(newLevelPrefix, sysDept.getParentId());
                        sysDept.setLevel(level);
                    }
                }

                for (SysDept sysDept : sysDeptList) {
                    sysDeptMapper.updateByPrimaryKeySelective(sysDept);
                }
//                sysAclModsysDeptMapperuleMapper.batchUpdateLevel(aclModuleList);
            }
        }

        sysDeptMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public void delete(int deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(sysDept, "待删除的部门不存在，无法删除");

        if (sysDeptMapper.countByParentId(deptId) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }

        if (sysUserMapper.countByDeptId(deptId) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }

        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
