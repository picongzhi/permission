package com.pcz.permission.controller;

import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.AclModuleParam;
import com.pcz.permission.service.SysAclModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {
    @Autowired
    private SysAclModuleService sysAclModuleService;

    @RequestMapping("/acl.page")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(@RequestBody AclModuleParam param) {
        sysAclModuleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(@RequestBody AclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonData.success();
    }
}
