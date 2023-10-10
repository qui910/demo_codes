package com.example.springbootdemo.controller;

import com.example.springbootdemo.event.EventPublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pang
 * @version 1.0
 * @date 2023-10-10 14:33
 * @since 1.8
 **/
@RestController
@RequestMapping("/event")
@Slf4j
public class EventController {

    @Autowired
    private EventPublish eventPublish;

    @GetMapping("/sendEvent")
    public String sendEvent(String message) {
        log.info("--Controller-sendEvent---{}-begin",Thread.currentThread().getName());
        long time = System.currentTimeMillis();
        log.info("--Controller-sendEvent---{}",time);
        eventPublish.sendUserEvent(message+"-"+ time);
        log.info("--Controller-sendEvent---end");
        return "success";
    }
}
