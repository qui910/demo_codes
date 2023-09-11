package com.example.springbootdemo.queue.demo1;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-07 14:53
 * @since 1.8
 **/
@Slf4j
public class QueueManager {

    /**
     * 这里使用Map 作为队列管理中心
     * <p>
     *  创建一个queue管理中心，所有创建的Queue在这里进行管理
     *  Map -> key : queue名称
     *  Map -> value : 队列
     * </p>
     */
    private static final Map<String, IQueue<Object>> QUEUE_MAP = new HashMap<>();

    /**
     * 从QueueManager获取 Queue
     * <p>
     *      加锁目的：防止同时创建相同名称的queue
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static synchronized IQueue<Object> getQueue(String queueName,Class<? extends IQueue> queueClass) {
        // 从map中根据名称获取队列，如果已经存在，则返回map中的队列
        IQueue<Object> queue = QUEUE_MAP.get(queueName);
        // 如果是第一次创建队列，则新建队列并放入map，然后将新建的队列返回
        if (queue == null) {
            try {
                queue = queueClass.newInstance();
            } catch (Exception e) {
                log.error("创建IQueue实例错误");
            }
            putQueue(queueName, queue);
            return QUEUE_MAP.get(queueName);
        }
        return queue;
    }

    /**
     * 将 Queue 放入 QueueManager
     * @param queueName 队列名称
     * @param queue  队列实例
     */
    private static synchronized void  putQueue(String queueName, IQueue<Object> queue) {
        QUEUE_MAP.put(queueName, queue);
    }
}
