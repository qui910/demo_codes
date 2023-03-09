package com.example.spring.ioc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 纯注解方式Spring工程的配置类
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:31
 * @since 1.8
 **/
@Configuration
@ComponentScan("com.example.spring.ioc")
@Scope("prototype")
public class AppConfig {
}
