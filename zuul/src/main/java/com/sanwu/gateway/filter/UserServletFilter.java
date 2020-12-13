package com.sanwu.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * <pre>
 * @Description:
 * servlet filter
 * </pre>
 *
 * @version v1.0
 * @ClassName: UserServletFilter
 * @Author: sanwu
 * @Date: 2020/12/13 0:46
 */
@Slf4j
@Order(2)
@WebFilter(filterName = "FirstFilter",urlPatterns = "/*")
public class UserServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("this is servlet filter");
    }


}
