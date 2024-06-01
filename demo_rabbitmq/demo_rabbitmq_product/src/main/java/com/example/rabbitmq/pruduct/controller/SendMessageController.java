package com.example.rabbitmq.pruduct.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.example.rabbitmq.pruduct.config.PortConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class SendMessageController {

    //使用RabbitTemplate,这提供了接收/发送等等方法
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private PortConfig portConfig;

    public static final String BUSINESS_EXCHANGE_NAME = "dead.letter.demo.simple.business.exchange";

    /**
     * 直联交换机
     */
    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        //此时路由值不能为空
        log.info("sendDirectMessage {} sendMessage：{}",portConfig.getPort(),map);
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return "ok";
    }

    @GetMapping("/sendDirectMessage1")
    public String sendDirectMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message broadcast, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        log.info("sendDirectMessage1 {} sendMessage：{}",portConfig.getPort(),map);
        rabbitTemplate.convertAndSend("broadcastDirectExchange", "log.info", map);
        return "ok";
    }


    @GetMapping("/sendTopicMessage1")
    public String sendTopicMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: M A N ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> manMap = new HashMap<>();
        manMap.put("messageId", messageId);
        manMap.put("messageData", messageData);
        manMap.put("createTime", createTime);
        log.info("sendTopicMessage1 {} sendMessage：{}",portConfig.getPort(),manMap);
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", manMap);
        return "ok";
    }

    @GetMapping("/sendTopicMessage2")
    public String sendTopicMessage2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: woman is all ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> womanMap = new HashMap<>();
        womanMap.put("messageId", messageId);
        womanMap.put("messageData", messageData);
        womanMap.put("createTime", createTime);
        log.info("sendTopicMessage2 {} sendMessage：{}",portConfig.getPort(),womanMap);
        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", womanMap);
        return "ok";
    }

    @GetMapping("/sendTopicBroadcastMessage1")
    public String sendTopicBroadcastMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: log.error ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> manMap = new HashMap<>();
        manMap.put("messageId", messageId);
        manMap.put("messageData", messageData);
        manMap.put("createTime", createTime);
        log.info("sendTopicBroadcastMessage1 {} sendMessage：{}",portConfig.getPort(),manMap);
        rabbitTemplate.convertAndSend("broadcastTopicExchange", "log.error", manMap);
        return "ok";
    }

    @GetMapping("/sendTopicBroadcastMessage2")
    public String sendTopicBroadcastMessage2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: log.info ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> manMap = new HashMap<>();
        manMap.put("messageId", messageId);
        manMap.put("messageData", messageData);
        manMap.put("createTime", createTime);
        log.info("sendTopicBroadcastMessage2 {} sendMessage：{}",portConfig.getPort(),manMap);
        rabbitTemplate.convertAndSend("broadcastTopicExchange", "log.info", manMap);
        return "ok";
    }


    @GetMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: testFanoutMessage ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        log.info("sendFanoutMessage {} sendMessage：{}",portConfig.getPort(),map);
        rabbitTemplate.convertAndSend("fanoutExchange", null, map);
        return "ok";
    }

    @GetMapping("/sendFanoutMessage1")
    public String sendFanoutMessage1() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: testBroadCastFanoutMessage ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        log.info("sendFanoutMessage1 {} sendMessage：{}",portConfig.getPort(),map);
        rabbitTemplate.convertAndSend("broadcastFanoutExchange", null, map);
        return "ok";
    }

    @GetMapping("sendDeadLetterMessage")
    public void sendMsg(String msg){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", msg);
        map.put("createTime", createTime);
        log.info("sendFanoutMessage1 {} sendMessage：{}",portConfig.getPort(),map);
        rabbitTemplate.convertAndSend(BUSINESS_EXCHANGE_NAME, null, map);
    }

    @GetMapping("sendDelayMsg1")
    public void sendDelayMsg1(String msg,  long delayTime){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", msg);
        map.put("createTime", createTime);
        log.info("sendFanoutMessage1 {} sendMessage：{}",portConfig.getPort(),map);

        // 创建消息属性
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(String.valueOf(delayTime*1000)); // 设置 TTL，单位为毫秒

        // 创建消息对象
        Message amqpMessage = new Message(ObjectUtil.serialize(map), messageProperties);

        // 发送消息
        rabbitTemplate.send("delay.queue.demo.business.exchange", "delay.queue.demo.business.queuec.routingkey",
                amqpMessage, new CorrelationData(String.valueOf(new Date().getTime())));
    }

}