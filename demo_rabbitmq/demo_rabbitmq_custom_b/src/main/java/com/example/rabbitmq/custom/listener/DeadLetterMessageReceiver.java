package com.example.rabbitmq.custom.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 死信队列的消费者
 * @author pang
 * @version 1.0
 * @date 2024-04-22 14:39
 * @since 1.8
 **/
@Slf4j
@Component
public class DeadLetterMessageReceiver {

    public static final String DEAD_LETTER_QUEUEA_NAME = "dead.letter.demo.simple.deadletter.queuea";
    public static final String DEAD_LETTER_QUEUEB_NAME = "dead.letter.demo.simple.deadletter.queueb";
    @RabbitListener(queues = DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Map testMessage,Message message, Channel channel) throws IOException {
        log.info("收到死信消息A：{}" , message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Map testMessage,Message message, Channel channel) throws IOException {
        log.info("收到死信消息B：{}" , message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
