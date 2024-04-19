package com.example.rabbitmq.custom.listener;

import com.example.rabbitmq.custom.config.PortConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//监听的队列名称 TestDirectQueue
@RabbitListener(queues = "TestDirectQueue")
@Slf4j
public class DirectReceiver {

    private final PortConfig portConfig;

    public DirectReceiver(PortConfig portConfig) {
        this.portConfig = portConfig;
    }

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("DirectReceiver-{}-消费者收到消息:{}" ,portConfig,testMessage.toString());
    }

}

