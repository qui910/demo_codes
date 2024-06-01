package org.example.designPatterns;

import lombok.extern.slf4j.Slf4j;
import org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4.SpringContextUtil3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-08-07 09:15
 * @since 1.8
 **/
@SpringBootApplication
@Slf4j
public class DesignPatternsDemoApplication {
    public static void main(String[] args) {
        log.info("begin===============================================");
        // 启动时，保存上下文，并保存为静态
        ConfigurableApplicationContext ac = SpringApplication.run(DesignPatternsDemoApplication.class, args);
        SpringContextUtil3.setAc(ac);
    }

}
