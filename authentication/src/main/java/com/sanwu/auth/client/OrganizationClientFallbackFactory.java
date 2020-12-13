package com.sanwu.auth.client;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: HystrixClientFallbackFactory
 * @Author: sanwu
 * @Date: 2020/12/13 13:27
 */

@Component
public class OrganizationClientFallbackFactory implements FallbackFactory<OrganizationClient> {

    @Override
    public OrganizationClient create(Throwable throwable) {
        return organizationId -> "fallback";
    }
}
