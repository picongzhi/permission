package com.pcz.permission.filter;

import com.pcz.permission.common.RequestHolder;
import com.pcz.permission.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author picongzhi
 */
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null) {
            response.sendRedirect("/signin.jsp");
            return;
        }

        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
