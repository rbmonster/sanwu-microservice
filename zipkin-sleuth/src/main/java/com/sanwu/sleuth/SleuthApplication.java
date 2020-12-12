package com.sanwu.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: SleuthApplication
 * @Author: sanwu
 * @Date: 2020/12/13 1:16
 */
@SpringBootApplication
@EnableZipkinServer
public class SleuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class,args);
    }
}
