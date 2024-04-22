package com.example.rabbitmq.custom.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 死信队列测试，业务消息接收者
 * @author pang
 * @version 1.0
 * @date 2024-04-22 10:02
 * @since 1.8
 **/
@Slf4j
@Component
public class BusinessMessageReceiver {

    public static final String BUSINESS_QUEUEA_NAME = "dead.letter.demo.simple.business.queuea";
    public static final String BUSINESS_QUEUEB_NAME = "dead.letter.demo.simple.business.queueb";

   @RabbitListener(queues = BUSINESS_QUEUEA_NAME)
   public void receiveA(Map testMessage,Message message, Channel channel) throws IOException {
       log.info("收到业务消息A：{}", testMessage);
       boolean ack = true;
       Exception exception = null;
       try {
           if (testMessage.get("messageData").toString().contains("deadletter")){
               throw new RuntimeException("dead letter exception");
           }
       } catch (Exception e){
           ack = false;
           exception = e;
       }

       if (!ack){
           log.error("消息消费发生异常，error msg:{}", exception.getMessage(), exception);
           channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
       } else {
           channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
       }
   }

   @RabbitListener(queues = BUSINESS_QUEUEB_NAME)
   public void receiveB(Map testMessage,Message message, Channel channel) throws IOException {
       log.info("收到业务消息B：{}" ,testMessage);
       channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
   }
}
