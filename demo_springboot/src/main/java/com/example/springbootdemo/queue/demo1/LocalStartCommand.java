package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-13 16:16
 * @since 1.8
 **/
@Component
@Slf4j
public class LocalStartCommand implements CommandLineRunner {
    @Autowired
    private LocalConsumer consumer;

    @Override
    public void run(String... args) throws Exception {
        // 屏蔽调试队写入阻塞问题
//        consumer.receiveMessageAndDelete();
//        log.info("LocalStartCommand初始化完成，线程：{}",Thread.currentThread().getName());
    }
}
