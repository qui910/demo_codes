package com.example.springbootdemo.controller;

import com.example.springbootdemo.queue.demo1.LocalConsumer;
import com.example.springbootdemo.queue.demo1.LocalProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-11 09:57
 * @since 1.8
 **/
@RestController
@RequestMapping("/mQueue")
@Slf4j
public class MQueueController {

    @Autowired
    private LocalProvider localProvider;

    @Autowired
    private LocalConsumer localConsumer;

    @GetMapping("/sendMessage")
    public String sendMessage(String message) {
        log.info("--Controller-sendMessage---{}-begin",Thread.currentThread().getName());
        long time = System.currentTimeMillis();
        log.info("--Controller-sendMessage---{}",time);
        localProvider.sendMessage(message+"-"+ time);
        log.info("--Controller-sendMessage---end");
        return "success";
    }

    @GetMapping("/getMessage")
    public String getMessage() {
        log.info("--Controller-getMessage---{}-begin",Thread.currentThread().getName());
        localConsumer.receiveMessageAndDelete();
        log.info("--Controller-getMessage---end");
        return "success";
    }
}