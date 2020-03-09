package com.pcz.permission.controller;

import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.RoleAclParam;
import com.pcz.permission.param.RoleParam;
import com.pcz.permission.service.SysRoleAclService;
import com.pcz.permission.service.SysRoleService;
import com.pcz.permission.service.SysTreeService;
import com.pcz.permission.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @RequestMapping(value = "/role.page", method = RequestMethod.GET)
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveRole(@RequestBody RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateRole(@RequestBody RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    @RequestMapping(value = "/roleTree.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") Integer roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    @RequestMapping(value = "/changeAcls.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData changeAcls(@RequestBody RoleAclParam param) {
        List<Integer> aclIdList = StringUtil.splitToIntList(param.getAclIds());
        sysRoleAclService.changeRoleAcls(param.getRoleId(), aclIdList);
        return JsonData.success();
    }
}
