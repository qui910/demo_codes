package com.example.springbootdemo.event;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author pang
 * @version 1.0
 * @date 2023-10-10 14:17
 * @since 1.8
 **/
@Component
@Slf4j
public class UserEventListener implements ApplicationListener<UserEvent> {
    @SneakyThrows
    @Override
    public void onApplicationEvent(UserEvent userEvent) {
        log.info("接收到消息【{}】,线程号:[{}]",userEvent,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(10);
        log.info("接收到消息完成");
    }
}
