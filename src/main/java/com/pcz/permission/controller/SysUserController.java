package com.pcz.permission.controller;

import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.UserParam;
import com.pcz.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(@RequestBody UserParam param) {
        sysUserService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(@RequestBody UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }
}
