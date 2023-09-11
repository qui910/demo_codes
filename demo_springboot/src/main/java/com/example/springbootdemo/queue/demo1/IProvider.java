package com.example.springbootdemo.queue.demo1;

/**
 * 消息生产者
 * @author pang
 * @version 1.0
 * @date 2023-09-07 15:51
 * @since 1.8
 **/
public interface IProvider<T> extends IWork {
    /**
     * 发送消息到指定队列
     * @param message 消息
     */
    void sendMessage(T message);
}
