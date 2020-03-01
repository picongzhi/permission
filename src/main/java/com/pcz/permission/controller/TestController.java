package com.pcz.permission.controller;

import com.pcz.permission.common.ApplicationContextHelper;
import com.pcz.permission.common.JsonData;
import com.pcz.permission.dao.SysAclModuleMapper;
import com.pcz.permission.exception.PermissionException;
import com.pcz.permission.param.TestVo;
import com.pcz.permission.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        log.info("hello");
        return JsonData.success("hello");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo) {
        SysAclModuleMapper sysAclModuleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        log.info("validate");
        BeanValidator.check(vo);
        return JsonData.success("validate");
    }
}
