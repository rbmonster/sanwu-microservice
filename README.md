# Spring cloud
## 基础知识
spring cloud 使用一个发行版的方式进行版本发布，目前有三个版本Angel、Brixton和Camden
```
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>Camden.SR5</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
```
spring-cloud-dependency与spring-boot-parent 版本对应：
- https://start.spring.io/actuator/info
# Spring config 配置中心

## 基本配置
maven版本
```
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
```

两种配置方式，本地文件配合与git的云上配置
- 本地文件配置application.yml
```
server:
   port: 8888
spring:
  profiles:
    active: native
  cloud:
     config:
       server:
           native:
              searchLocations: classpath:config/licensingservice
```

- git云上文件配置application.yml
```
server:
  port: 8888
spring:
  cloud:
    config:
      server:
        encrypt.enabled: false
        git:
          uri: https://github.com/carnellj/config-repo/
          searchPaths: licensingservice,organizationservice
          username: native-cloud-apps
          password: 0ffended
```

BootStrap配置：
```
spring:
  application:
    name: configserver

```
### 基本使用
直接访问
- `http://localhost:8888/licensingservice/default`
- `http://localhost:8888/{folder}/{version}`

## 功能

### 刷新配置@RefreshScope
普通的spring boot 可以通过访问 http://localhost:8080/refresh 刷新配置
```
@SpringBootApplication
@EnableConfigServer
@RefreshScope
public class ConfigServerApplication { }


<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### spring cloud应用刷新配置

1. 使用spring cloud bus 的推送机制。
2. 使用Eureka，可以使用脚本调用每个服务节点的/refresh 方法。

### 密码加密
安装Oracle JCE jar

配置中心通过post请求加密解密：
`http://localhost:8888/encrypt`
`http://localhost:8888/decrypt`

客户端服务器：
1. 设置配置中心不要解密属性
2. 客户端设置对应秘钥
3. 加入maven  

# Spring eureka 注册中心
## 基本配置

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

# Spring netflix hystrix客户端


# Spring netflix zuul 网关
代理服务请求

拥有前置过滤器、路由过滤器及后置过滤器
1. 前置过滤器：请求到服务网关的第一个过滤器，可以添加一个UUID用于跟踪请求。
2. 路由过滤器：可以决定是否按比例代理到不同的服务。
3. 后置过滤器：请求到服务返回响应的过滤器，用于一些请求的后置处理