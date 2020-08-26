package com.pcz.permission.service.impl;

import com.pcz.permission.beans.LogType;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysLogMapper;
import com.pcz.permission.model.*;
import com.pcz.permission.service.SysLogService;
import com.pcz.permission.util.IpUtil;
import com.pcz.permission.util.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author picongzhi
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void saveDeptLog(SysDept before, SysDept after) {
        saveLog(LogType.TYPE_DEPT, after == null ? before.getId() : after.getId(), before, after);
    }

    @Override
    public void saveUserLog(SysUser before, SysUser after) {
        saveLog(LogType.TYPE_USER, after == null ? before.getId() : after.getId(), before, after);
    }

    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        saveLog(LogType.TYPE_ACL_MODULE, after == null ? before.getId() : after.getId(), before, after);
    }

    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {
        saveLog(LogType.TYPE_ACL, after == null ? before.getId() : after.getId(), before, after);
    }

    @Override
    public void saveRoleLog(SysRole before, SysRole after) {
        saveLog(LogType.TYPE_ROLE, after == null ? before.getId() : after.getId(), before, after);
    }

    @Override
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        saveLog(LogType.TYPE_ROLE_ACL, roleId, before, after);
    }

    @Override
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        saveLog(LogType.TYPE_ROLE_USER, roleId, before, after);
    }

    private void saveLog(int type, int id, Object before, Object after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(type);
        sysLog.setTargetId(id);
        sysLog.setOldValue(before == null ? "" : JsonMapper.objectToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objectToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogMapper.insert(sysLog);
    }
}
