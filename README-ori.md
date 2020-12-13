# Spring cloud 基于Netflix
工程启动顺序：
1. config
2. eureka
3. organization、Auth、zuul

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
# config 配置中心

## 基本配置

### 配置中心应用
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

### 客户端应用
添加maven
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

使用BootStrap.yml用于在实际的spring 上下文初始化之前把配置中心的配置加载进来。
```
spring:
  application:
    name: authentication
  profiles:
    active:
      default
  cloud:
    config:
      enabled: true
      uri: http://localhost:8888
      fail-fast: true
      password: ${CONFIG_SERVICE_PASSWORD}
      username: user

```

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

#### 加密认证说明
1. 可以使用spring security作为认证的方式。
2. 可以使用非对称加密的方式认证。
3. 使用云服务如git 云认证。


# eureka 注册中心
1. 服务注册
2. 服务地址的客户端查找
3. 信息共享
4. 健康监测

访问地址：http://localhost:8761/

## 基本配置
- maven 配置
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

注解@EnableEurekaServer 声明是个注册中心


### 客户端
@EnableDiscoveryClient 开启服务注册

# netflix hystrix与OpenFeign客户端方法断路器

## netflix hystrix
### 基础
1. @EnableCircuitBreaker 开启断路器
2. maven 依赖
   - ``` 
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
     </dependency>
     ```
### 定时中断
@HystrixCommand 标记方法使用断路器管理
> 断路器将包装方法的调用，默认调用时间超过1000ms时，中断调用抛出HystrixRuntimeException的异常。

设置超时时间
```
@HystrixCommand(fallbackMethod="fallback",
	commandProperties = {
	     @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000" )
	}
)
```
### 后备处理
请求时间过长，调用备选的方法，
```
 @HystrixCommand(fallbackMethod = "authInfoFallback")
    @GetMapping("/getInfo")
    public String authInfo() {
        return name;
    }

    public String authInfoFallback() {
        return "fallback";
    }
```

### 舱壁模式
定义：将远程调用的资源隔离在自己的线程池中，以控制单个表现不佳的服务，不会是容器崩溃。

Hystrix 使用线程池来委派索引对远程服务的请求。在默认情况下，所有的hystrix命令都将共享同一个线程池来处理请求（默认线程数为10）。

```
@HystrixCommand(fallbackMethod = "authInfoFallback", 
        threadPoolKey = "authInfo",
        threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "30"),
                @HystrixProperty(name = "maxQueueSize", value = "10")
        },
        // 断路器逻辑属性
        commandProperties = {
                // 请求的最小次数
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                // 错误阀值
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                // 跳闸后，检测远程资源是否恢复的时间间隔
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                // 服务调用的时间窗口 默认10s
                @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                // 监控的时间窗口中维护度量桶的数量。
                @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
        })
```
- threadPoolKey表示想建立一个新的线程池。
- threadPoolProperties线程池的相关属性，核心线程数，最大队列数量。本质上与线程池创建过程一致，按照队列数量设置选择不同的等待队列。
- queueSizeRejectionThreshold 用于动态更改队列的大小，只有一开始设置大于零，该属性才有用。

计算线程池的大小公式：
```
服务在健康状态时每秒支撑的最大请求数 * 第99百分位延迟时间（秒为单位） + 用于缓存的少量额外线程
```

断路器整体逻辑：
1. 默认开启一个10s的时间窗口，时间窗口两属性最小调用次数、错误调用次数
2. 若调用未达到最小调用次数，无论调用是否错误，不触发断路器逻辑。
3. 若达到最小调用次数，则计算错误调用次数占比即错误阈值(默认50%)，若达到错误阈值，请求全部跳闸走fallback或直接返回。
4. 跳闸情况，默认每5s让一个请求通过，查看远程资源是否恢复，若恢复则恢复调用，否则重复5s调用。

两种隔离策略：Thread(线程)、semaphore(信号量)。
> 默认策略线程，信号量属于较轻的隔离方式。  


**自定义并发策略**HystrixConcurrencyStrategy，重写线程池创建的方式，提供回调。


## OpenFeign
### 基本配置
1. @EnableFeignClients 注解
2. maven 
  - ```
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    ```

demo
```
// 一个跨服务调用的url
// http://localhost:8090/auth/cross2Organization?organizationId=123

@LoadBalancerClient
@FeignClient(name = "organization", fallback = OrganizationClientFallbackFactory.class)
public interface OrganizationClient {

    @GetMapping(value = "/organization/v1/organizations/{organizationId}")
    String organization(@PathVariable("organizationId") String organizationId);
}
```

1. 便捷的http调用客户端，支持spring mvc 的所有messageConverter
2. 通过@Configuration配置自定义属性
3. 继承服务注册的功能，如上述仅name属性指明了调用的服务节点，直接调用目标服务。
4. 兼容后备及负载均衡功能，可以通过引入Ribbon实现


# Spring netflix zuul 网关
## 过滤器
> 代理服务请求，可以自定义filter通过semaphore工具类实现限流

拥有前置过滤器、路由过滤器及后置过滤器，实现方法继承ZuulFilter
1. 前置过滤器：请求到服务网关的第一个过滤器，可以添加一个UUID用于跟踪请求。
2. 路由过滤器：可以决定是否按比例代理到不同的服务。
3. 后置过滤器：请求到服务返回响应的过滤器，用于一些请求的后置处理

实现：
```
public class TrackingFilter extends ZuulFilter {

    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    @Override
    public String filterType() {
        log.info("this is filterType");
        return PRE_FILTER_TYPE;
    }
}
```

## 限流
zuul 限流实现：`https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit/blob/master/README.adoc#overview`

引入依赖
```
<dependency>
    <groupId>com.marcosbarbero.cloud</groupId>
    <artifactId>spring-cloud-zuul-ratelimit</artifactId>
    <version>2.4.0.RELEASE</version>
</dependency>
```

添加配置
- limit: 请求数量限制
- quota: 请求窗口时间长度
- refresh-interval: 限制后更新周期
```
zuul:
  ignoredServices: '*'
  host:
    connect-timeout-millis: 20000
    socket-timeout-millis: 20000
  routes:
    authentication:
        path: /auth/**
        serviceId: authentication
        stripPrefix: false
        sensitiveHeaders: Cookie,Set-Cookie
  ratelimit:
    policy-list:
      authentication:
        - limit: 5
          quota: 30
          refresh-interval: 60
          type:
            - user
            - origin
            - url
            - http_method
```