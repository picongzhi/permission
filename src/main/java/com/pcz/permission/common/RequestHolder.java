package com.pcz.permission.common;

import com.pcz.permission.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author picongzhi
 */
public class RequestHolder {
    private static final ThreadLocal<SysUser> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();

    public static void add(SysUser sysUser) {
        USER_HOLDER.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        REQUEST_HOLDER.set(request);
    }

    public static SysUser getCurrentUser() {
        return USER_HOLDER.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return REQUEST_HOLDER.get();
    }

    public static void remove() {
        USER_HOLDER.remove();
        REQUEST_HOLDER.remove();
    }
}
