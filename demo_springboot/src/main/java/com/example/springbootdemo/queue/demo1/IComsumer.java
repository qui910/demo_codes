package com.example.springbootdemo.queue.demo1;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 19:05
 * @since 1.8
 **/
public interface IComsumer<T> extends IWork {
    /**
     * 接收消息并从队列中移除
     * @return 消息
     */
    void receiveMessageAndDelete();
}
