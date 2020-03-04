package com.pcz.permission.controller;

import com.pcz.permission.model.SysUser;
import com.pcz.permission.service.SysUserService;
import com.pcz.permission.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author picongzhi
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SysUser sysUser = sysUserService.findByKeyword(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else if (sysUser == null) {
            errorMsg = "查询不到指定的用户";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
        } else {
            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.page");
            }
        }

        if (StringUtils.isNotBlank(errorMsg)) {
            request.setAttribute("error", errorMsg);
            request.setAttribute("username", username);
            if (StringUtils.isNotBlank(ret)) {
                request.setAttribute("ret", ret);
            }
            String path = "/signin.jsp";
            request.getRequestDispatcher(path).forward(request, response);
        }
    }
}