package com.example.springbootdemo.queue.demo1;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 15:17
 * @since 1.8
 **/
public interface IQueue<T> {
    /**
     * 添加消息
     * @param t 消息
     */
    void putMessage(T t);

    /**
     * 获得消息(获取首条消息并删除)
     * @return 消息
     */
    T pollFirst();

    /**
     * 获得消息(获取首条消息但不删除)
     * @return 消息
     */
    T peekFirst();

    /**
     * 队列中是否存在消息
     * @return true 存在消息；false 不存在消息
     */
    boolean isReady();

    /**
     * 返回队列当前消息数
     * @return 数量
     */
    int getSize();
}
