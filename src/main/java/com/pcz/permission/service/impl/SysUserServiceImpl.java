package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.pcz.permission.dao.SysUserMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.UserParam;
import com.pcz.permission.service.SysUserService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.MD5Util;
import com.pcz.permission.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author picongzhi
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void save(UserParam param) {
        checkParam(param);

        String password = PasswordUtil.randomPassword();
        password = "123456";
        String encryptedPassword = MD5Util.encrypt(password);

        SysUser sysUser = SysUser.builder()
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .password(encryptedPassword)
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        sysUser.setOperator("system");
        sysUser.setOperateIp("127.0.0.1");
        sysUser.setOperateTime(new Date());

        sysUserMapper.insertSelective(sysUser);
    }

    @Override
    public void update(UserParam param) {
        checkParam(param);

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        SysUser after = SysUser.builder()
                .id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .mail(param.getMail())
                .deptId(param.getDeptId())
                .status(param.getStatus())
                .remark(param.getRemark()).build();
        after.setOperator("system");
        after.setOperateIp("127.0.0.1");
        after.setOperateTime(new Date());

        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    private void checkParam(UserParam param) {
        BeanValidator.check(param);
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        if (checkTelephoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
    }

    private boolean checkEmailExist(String mail, Integer userId) {
        return false;
    }

    private boolean checkTelephoneExist(String telephone, Integer userId) {
        return false;
    }
}
