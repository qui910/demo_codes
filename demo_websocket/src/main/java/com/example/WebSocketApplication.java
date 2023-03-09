package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * WebSocket测试启动
 *
 * @author pangruidong
 * @version 1.0
 * @date 2023-01-05 14:04
 * @since 1.8
 **/
@SpringBootApplication
@EnableScheduling
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
    }
}
