package com.pcz.permission.controller;

import com.pcz.permission.common.JsonData;
import com.pcz.permission.param.DeptParam;
import com.pcz.permission.service.SysDeptService;
import com.pcz.permission.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveDept(@RequestBody DeptParam deptParam) {
        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    @RequestMapping(value = "/tree.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData tree() {
        return JsonData.success(sysTreeService.deptTree());
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateDept(@RequestBody DeptParam deptParam) {
        sysDeptService.update(deptParam);
        return JsonData.success();
    }

    @RequestMapping("/dept.page")
    public ModelAndView page() {
        return new ModelAndView("dept");
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonData delete(@RequestParam("id") Integer id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }
}
