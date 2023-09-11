package com.example.springbootdemo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-11 13:45
 * @since 1.8
 **/
@Service
@Slf4j
public class TestCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("TestCommandLineRunner线程：{}",Thread.currentThread().getName());
    }
}
