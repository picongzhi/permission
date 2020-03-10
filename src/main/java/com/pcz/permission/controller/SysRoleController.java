package com.pcz.permission.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pcz.permission.common.JsonData;
import com.pcz.permission.model.SysUser;
import com.pcz.permission.param.RoleAclParam;
import com.pcz.permission.param.RoleParam;
import com.pcz.permission.param.RoleUserParam;
import com.pcz.permission.service.*;
import com.pcz.permission.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysUserService sysUserService;

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

    @RequestMapping(value = "/users.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getUserListByRoleId(roleId);
        selectedUserList = selectedUserList.stream()
                .filter(sysUser -> sysUser.getStatus() == 1)
                .collect(Collectors.toList());
        Set<Integer> selectedUserIdSet = selectedUserList.stream()
                .map(SysUser::getId)
                .collect(Collectors.toSet());

        List<SysUser> allUserList = sysUserService.getAll();
        List<SysUser> unSelectedUserList = Lists.newArrayList();
        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unSelectedUserList.add(sysUser);
            }
        }

        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unSelectedUserList);

        return JsonData.success(map);
    }

    @RequestMapping(value = "/changeUsers.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData changeUsers(@RequestBody RoleUserParam param) {
        List<Integer> userIdList = StringUtil.splitToIntList(param.getUserIds());
        sysRoleUserService.changeRoleUsers(param.getRoleId(), userIdList);
        return JsonData.success();
    }
}
