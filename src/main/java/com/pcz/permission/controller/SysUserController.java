package com.pcz.permission.controller;

import com.google.common.collect.Maps;
import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.UserParam;
import com.pcz.permission.service.SysRoleService;
import com.pcz.permission.service.SysTreeService;
import com.pcz.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveUser(@RequestBody UserParam param) {
        sysUserService.save(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateUser(@RequestBody UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/page.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData page(@RequestParam("deptId") Integer deptId, PageQuery pageQuery) {
        return JsonData.success(sysUserService.getPageByDeptId(deptId, pageQuery));
    }

    @RequestMapping(value = "/acls.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData acls(@RequestParam("userId") Integer userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));

        return JsonData.success(map);
    }
}
