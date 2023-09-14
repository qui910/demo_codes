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
     * 内存队列最大容量
     * <p>
     *    操作此容量时，阻塞写入线程。当小于此容量时，唤醒写入线程。
     * </p>
     */
    private final static int QUEUE_MAX_SIZE = 5;

    /**
     * 放入消息
     * 注意：这里加锁是为了防止并发操作，因为LinkedList本身是线程不安全的
     * @param t 消息
     */
    @Override
    public void putMessage(T t) {
        synchronized (localQueue) {
            log.info("当前队列长度：{}",localQueue.size());
            // 如果队列在等待，则执行唤醒
            if (localQueue.isEmpty()) {
                log.info("唤醒队列...");
                localQueue.notifyAll();
            }
            if (localQueue.size()>QUEUE_MAX_SIZE) {
                try {
                    localQueue.wait();
                } catch (InterruptedException e) {
                    log.error("消息队列写入阻塞唤醒异常",e);
                }
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
            log.info("当前队列长度：{}",localQueue.size());
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
                if (localQueue.size()<=QUEUE_MAX_SIZE) {
                    localQueue.notifyAll();
                }
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