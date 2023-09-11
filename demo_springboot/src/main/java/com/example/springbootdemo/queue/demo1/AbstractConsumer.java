package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 19:09
 * @since 1.8
 **/
@Slf4j
@Service
public abstract class AbstractConsumer<T> implements IComsumer<T> {

    @Async
    @Override
    @SuppressWarnings("unchecked")
    public void receiveMessageAndDelete() {
        log.info("异步方法开始执行，线程：{}",Thread.currentThread().getName());
        // 获取到queue
        IQueue<T> queue = (IQueue<T>) QueueManager.getQueue(getQueueName(),getQueueClass());
        // 如果队列中存在消息，则一直处于消费状态
        while (true) {
            // 消费消息, 执行巴拉巴拉一大堆业务处理后删除
            try {
                T t = queue.pollFirst();
                log.info("消费者从队列[{}]接收到的消息为:[{}]",getQueueClass(),t.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
