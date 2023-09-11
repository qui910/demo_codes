package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 基于LinkedList实现消息队列
 * @author pang
 * @version 1.0
 * @date 2023-09-07 14:20
 * @since 1.8
 **/
@Slf4j
public abstract class AbstractQueue<T> implements IQueue<T> {

    private final LinkedList<T> localQueue = new LinkedList<>();

    /**
     * 放入消息
     * 注意：这里加锁是为了防止并发操作，因为LinkedList本身是线程不安全的
     * @param t 消息
     */
    @Override
    public void putMessage(T t) {
        synchronized (localQueue) {
            // 如果队列在等待，则执行唤醒
            if (localQueue.isEmpty()) {
                log.info("唤醒队列...");
                localQueue.notifyAll();
            }
            // 将消息放入队列
            localQueue.push(t);
        }
    }

    /**
     * 获得消息(获取首条消息并删除)
     */
    @Override
    public T pollFirst() {
        synchronized (localQueue) {
            // 如果队列中没有消息，则处于堵塞状态，有消息则进行消费
            if (localQueue.isEmpty()) {
                try {
                    log.info("队列中没有数据，开始等待....");
                    localQueue.wait();
                    // 被唤醒后，继续往下执行
                    return localQueue.pollFirst();
                } catch (InterruptedException e) {
                    log.error("消息队列阻塞唤醒异常",e);
                }
            } else {
                return localQueue.pollFirst();
            }
        }
        return null;
    }

    /**
     * 获得消息(获取首条消息但不删除)
     */
    @Override
    public T peekFirst() {
        synchronized (localQueue) {
            return localQueue.peekFirst();
        }
    }

    /**
     * 队列中是否存在消息
     * @return true 存在消息；false 不存在消息
     */
    @Override
    public boolean isReady() {
        return !localQueue.isEmpty();
    }

    @Override
    public int getSize() {
        return localQueue.size();
    }
}