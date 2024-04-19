package com.example.rabbitmq.custom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// 配置类 注入port的值
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "server")
public class PortConfig {
    private int port;
}
