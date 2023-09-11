package com.example.springbootdemo.queue.demo1;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 19:06
 * @since 1.8
 **/
public interface IWork {
    /**
     * 获取队列名称
     * @return 队列名称
     */
    String getQueueName();

    /**
     * 获取队列类类型
     * @return 类类型
     */
    Class<? extends IQueue> getQueueClass();
}
