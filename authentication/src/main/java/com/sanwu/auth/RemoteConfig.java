package com.sanwu.auth;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

/**
 * <pre>
 * @Description:
 * TODO
 * </pre>
 *
 * @version v1.0
 * @ClassName: RemoteClient
 * @Author: sanwu
 * @Date: 2020/12/12 20:55
 */
public class RemoteConfig {

    @Value("dy.name")
    String name;
}
