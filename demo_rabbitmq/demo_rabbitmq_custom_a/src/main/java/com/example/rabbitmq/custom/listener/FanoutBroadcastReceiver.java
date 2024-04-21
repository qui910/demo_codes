package com.example.rabbitmq.custom.listener;

import com.example.rabbitmq.custom.config.PortConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(bindings = @QueueBinding(
        // 使用了 SpEL 来指定队列名称
        value = @Queue(value = "fanout.A" + "-" + "#{ T(java.util.UUID).randomUUID() }"),
        exchange = @Exchange(value = "broadcastFanoutExchange",type="fanout")
))
@Slf4j
public class FanoutBroadcastReceiver {
    @Autowired
    private PortConfig portConfig;

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("FanoutBroadcastReceiver-{}-消费者收到消息:{}" ,portConfig.getPort(),testMessage.toString());
    }
}
