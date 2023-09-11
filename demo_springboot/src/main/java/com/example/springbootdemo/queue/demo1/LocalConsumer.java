package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-11 09:22
 * @since 1.8
 **/
@Slf4j
@Service
public class LocalConsumer extends AbstractConsumer<String> {
    @Override
    public String getQueueName() {
        return "localQueue";
    }
    @Override
    public Class<? extends IQueue> getQueueClass() {
        return LocalQueue.class;
    }

    // 需要在LocalConsumer构造后，立即启动异步方法receiveMessageAndDelete时，不能是同一个类的不同方法，否则无效
//    @PostConstruct
//    public void init() {
//        log.info("LocalConsumer初始化完成，线程：{}",Thread.currentThread().getName());
//        receiveMessageAndDelete();
//    }
}
