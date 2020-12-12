package com.sanwu.gateway;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <pre>
 * @Description:
 * 仅仅为了注入bean
 * </pre>
 *
 * @version v1.0
 * @ClassName: DyConfiguration
 * @Author: sanwu
 * @Date: 2020/12/13 0:49
 */
@Configuration
public class DyConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
