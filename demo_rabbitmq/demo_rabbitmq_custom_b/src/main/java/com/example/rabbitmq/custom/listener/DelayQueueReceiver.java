package com.example.rabbitmq.custom.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 延迟队列（死信队列）接收器
 * @author pang
 * @version 1.0
 * @date 2024-04-25 14:56
 * @since 1.8
 **/
@Component
@Slf4j
public class DelayQueueReceiver {

    public static final String DEAD_LETTER_QUEUEC_NAME = "delay.queue.demo.deadletter.queuec";

    @RabbitListener(queues = DEAD_LETTER_QUEUEC_NAME)
    public void receiveC(Message message, Channel channel) throws IOException {
        log.info("当前时间：{},死信队列C收到消息：{}", DateUtil.date(), ObjectUtil.deserialize(message.getBody(),Map.class));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
