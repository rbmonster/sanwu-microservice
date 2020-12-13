package com.sanwu.auth.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: OrganizationClient
 * @Author: sanwu
 * @Date: 2020/12/13 13:23
 */
@LoadBalancerClient
@FeignClient(name = "organization", fallback = OrganizationClientFallbackFactory.class)
public interface OrganizationClient {

    /**
     * 查询找不到服务报错
     * @param organizationId
     * @return
     */
    @GetMapping(value = "/organization/v1/organizations/{organizationId}")
    String organization(@PathVariable("organizationId") String organizationId);
}
