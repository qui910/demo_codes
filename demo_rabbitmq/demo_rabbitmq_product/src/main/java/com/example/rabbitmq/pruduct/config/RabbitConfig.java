package com.example.rabbitmq.pruduct.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        // 消息确认接口，只要发送到交换机，即为成功。触发时机:
        // 1. 消息推送到server，但是在server里找不到交换机  ack为false
        // 2. 消息推送到server，找到交换机了，但是没找到队列  ack为true
        // 3. 消息推送到sever，交换机和队列啥都没找到   ack为false
        // 4. 消息推送成功   ack为true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("ConfirmCallback-相关数据：{}",correlationData);
            log.info("ConfirmCallback-确认情况：{}",ack);
            log.info("ConfirmCallback-原因：{}",cause);
        });

        // 消息返回接口，只要消息推送到交换机，没有投递给消费者，就会触发这个接口。触发时机：
        // 1. 消息推送到server，找到交换机了，但是没找到队列
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("ReturnCallback-消息：{}",message);
            log.info("ReturnCallback-回应码："+replyCode);
            log.info("ReturnCallback-回应信息：{}",replyText);
            log.info("ReturnCallback-交换机：{}",exchange);
            log.info("ReturnCallback-路由键：{}",routingKey);
        });
        return rabbitTemplate;
    }
}

