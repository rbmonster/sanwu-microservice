package com.sanwu.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * <pre>
 * @Description:
 * 前置过滤器
 * </pre>
 *
 * @version v1.0
 * @ClassName: TrackingFilter
 * @Author: sanwu
 * @Date: 2020/12/13 0:08
 */
@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {


    final FilterUtils filterUtils;

    @Autowired
    public TrackingFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    /**
     * 区分前置过滤器 路由过滤器
     * 及 应用请求完成之后的 后置过滤器
     * @return
     */
    @Override
    public String filterType() {
        log.info("this is filterType");
        return filterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        log.info("this is filterOrder");
        return 0;
    }

    /**
     * 返回boolean 判断是否执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        log.info("this is shouldFilter");
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("this is run method");
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = filterUtils.getAuthToken();
        String correlationId = filterUtils.getCorrelationId();
        // 为请求设置一个唯一跟踪ID
        if (Objects.isNull(correlationId)) {
            filterUtils.setCorrelationId(UUID.randomUUID().toString());
        }
        return null;
    }
}
