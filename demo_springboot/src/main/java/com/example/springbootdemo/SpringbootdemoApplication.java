package com.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * spring boot启动
 * @author pang
 */
@SpringBootApplication
// 这里必须使用proxyTargetClass=true,因为LocalConsumer是泛型子类
// 否则SpringBoot启动时会提示错误 The bean 'localConsumer' could not be injected as a 'com.example.springbootdemo.queue.demo1
// .LocalConsumer'
// because it is a JDK dynamic proxy that implements:
@EnableAsync(proxyTargetClass=true)
public class SpringbootdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootdemoApplication.class, args);
    }
}
