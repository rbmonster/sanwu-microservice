package com.sanwu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: GatewayApplication
 * @Author: sanwu
 * @Date: 2020/12/12 23:47
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
// 导致正常的网关过滤器失效
//@ServletComponentScan
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class);
    }
}
