package com.pcz.permission.service.impl;

import com.google.common.base.Preconditions;
import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.beans.PageResult;
import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.dao.SysAclMapper;
import com.pcz.permission.exception.ParamException;
import com.pcz.permission.model.SysAcl;
import com.pcz.permission.param.AclParam;
import com.pcz.permission.service.SysAclService;
import com.pcz.permission.util.BeanValidator;
import com.pcz.permission.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author picongzhi
 */
@Service
public class SysAclServiceImpl implements SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;

    @Override
    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }

        SysAcl sysAcl = SysAcl.builder()
                .aclModuleId(param.getAclModuleId())
                .name(param.getName())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAcl.setOperateTime(new Date());

        sysAclMapper.insertSelective(sysAcl);
    }

    @Override
    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }

        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder()
                .id(param.getId())
                .aclModuleId(param.getAclModuleId())
                .name(param.getName())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    private String generateCode() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date()) + "_" + (int) (Math.random() * 100);
    }

    @Override
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery query) {
        BeanValidator.check(query);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count == 0) {
            return PageResult.<SysAcl>builder().build();
        }

        List<SysAcl> sysAclList = sysAclMapper.getPageByAclModuleId(aclModuleId, query);
        return PageResult.<SysAcl>builder()
                .data(sysAclList)
                .total(count)
                .build();
    }
}
