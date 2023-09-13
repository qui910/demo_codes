package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 只有单独使用其他类，才能在实例化后立即启动异步方法
 * @author pang
 * @version 1.0
 * @date 2023-09-11 11:24
 * @since 1.8
 **/
@Slf4j
@Component
public class LocalStartConsumer {

    @Autowired
    private LocalConsumer consumer;

    @PostConstruct
    public void init() {
//        log.info("LocalStartConsumer初始化完成，线程：{}",Thread.currentThread().getName());
//        consumer.receiveMessageAndDelete();
    }
}
