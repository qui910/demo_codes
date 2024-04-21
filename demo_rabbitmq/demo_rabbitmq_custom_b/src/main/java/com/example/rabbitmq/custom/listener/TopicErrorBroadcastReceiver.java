package com.example.rabbitmq.custom.listener;

import com.example.rabbitmq.custom.config.PortConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 接收error消息
 */
@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue, // 不指定队列名称，使用默认临时队列
        exchange = @Exchange(value = "broadcastTopicExchange",type="topic"),
        key = "log.error"
))
@Slf4j
public class TopicErrorBroadcastReceiver {
    @Autowired
    private PortConfig portConfig;
    @RabbitHandler
    public void process(Map testMessage) {
        log.info("TopicErrorBroadcastReceiver-{}-消费者收到消息:{}" ,portConfig.getPort(),testMessage.toString());
    }
}
