package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.beans.PageResult;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysUserMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.UserParam;
import com.pcz.permission.service.SysUserService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.IpUtil;
import com.pcz.permission.util.MD5Util;
import com.pcz.permission.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
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
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
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
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    private boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    @Override
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    @Override
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);

        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> sysUserList = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder()
                    .data(sysUserList)
                    .total(count)
                    .build();
        }

        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }
}
