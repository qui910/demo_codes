package com.example.springbootdemo.queue.demo1;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 18:50
 * @since 1.8
 **/
@Component
public class LocalProvider extends AbstractProvider<String> {
    @Override
    public String getQueueName() {
        return "localQueue";
    }

    @Override
    public Class<? extends IQueue> getQueueClass() {
        return LocalQueue.class;
    }
}
