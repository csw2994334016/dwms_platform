package com.three.dwms.filter;

import com.three.dwms.common.ContextHelper;
import com.three.dwms.common.RequestHolder;
import com.three.dwms.entity.sys.SysUser;
import com.three.dwms.service.sys.SysCoreService;
import com.three.dwms.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class AclControlFilter implements Filter {

    private final static String noAuthUrl = "/auth/*";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        if (servletPath.startsWith(noAuthUrl)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

//        log.info("sessionId: " + request.getSession().getId());

        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");

        if (sysUser == null) {
            log.error("someone visit: {}, but no login, parameter: {}", servletPath, JsonMapper.obj2String(requestMap));
            request.getRequestDispatcher("/auth/noLogin").forward(request, response);
            return;
        }

        RequestHolder.add(sysUser);
        RequestHolder.add(request);

        SysCoreService sysCoreService = ContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.error("{} visit: {}, but no access auth, parameter: {}", JsonMapper.obj2String(sysUser), servletPath, JsonMapper.obj2String(requestMap));
            request.getRequestDispatcher("/auth/noAuth").forward(request, response);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
