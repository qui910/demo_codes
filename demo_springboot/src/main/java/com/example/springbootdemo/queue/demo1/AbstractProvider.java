package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 18:21
 * @since 1.8
 **/
@Slf4j
public abstract class AbstractProvider<T> implements IProvider<T>{

    @Override
    @SuppressWarnings("unchecked")
    public void sendMessage(T message) {
        // 获取到queue
        IQueue<T> queue = (IQueue<T>) QueueManager.getQueue(getQueueName(),getQueueClass());
        // 放入消息
        queue.putMessage(message);
        log.info("队列[{}]的消息数[{}]",getQueueName(),queue.getSize());
    }
}
