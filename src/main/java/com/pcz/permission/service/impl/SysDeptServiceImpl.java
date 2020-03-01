package com.pcz.permission.service.impl;

import com.pcz.permission.dao.SysDeptMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysDept;
import com.pcz.permission.param.DeptParam;
import com.pcz.permission.service.SysDeptService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.LevelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author picongzhi
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

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
        sysDept.setOperator("system");
        sysDept.setOperateIp("127.0.0.1");
        sysDept.setOperateTime(new Date());

        sysDeptMapper.insertSelective(sysDept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return true;
    }

    private String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }

        return sysDept.getLevel();
    }
}
