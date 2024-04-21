package com.example.rabbitmq.custom.listener;

import com.example.rabbitmq.custom.config.PortConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RabbitListener(queues = "fanout.B")
@Slf4j
public class FanoutReceiverB {

    @Autowired
    private PortConfig portConfig;

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("FanoutReceiverB-{}-消费者收到消息:{}" ,portConfig.getPort(),testMessage.toString());
    }
}
