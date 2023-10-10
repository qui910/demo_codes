package com.example.springbootdemo.event;

/**
 * @author pang
 * @version 1.0
 * @date 2023-10-10 14:09
 * @since 1.8
 **/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventPublish {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void sendUserEvent(String message) {
        log.info("发送事件[{}],线程[{}]",message,Thread.currentThread().getName());
        UserEvent<String> userEvent = new UserEvent<>(message);
        // 发布事件
        publisher.publishEvent(userEvent);
        log.info("发送事件完成");
    }
}
