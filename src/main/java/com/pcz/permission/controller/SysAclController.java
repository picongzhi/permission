package com.pcz.permission.controller;

import com.pcz.permission.beans.PageQuery;
import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.AclParam;
import com.pcz.permission.service.SysAclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;

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
}
