package com.pcz.permission.controller;

import com.google.common.collect.Maps;
import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.common.JsonData;
import com.pcz.permission.model.SysRole;
import com.pcz.permission.param.AclParam;
import com.pcz.permission.service.SysAclService;
import com.pcz.permission.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveAcl(@RequestBody AclParam param) {
        sysAclService.save(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateAcl(@RequestBody AclParam param) {
        sysAclService.update(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/page.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData page(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery query) {
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, query));
    }

    @RequestMapping(value = "/acls.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") Integer aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> sysRoleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", sysRoleList);
        map.put("users", sysRoleService.getUserListByRoleList(sysRoleList));

        return JsonData.success(map);
    }
}
