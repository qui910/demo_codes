package com.example.spring.condition;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试Spring的@Conditional注解，通过不同的condition注入不同对象
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:12
 * @since 1.8
 **/
@Slf4j
public class ConditionTest {

    @Test
    public void getPerson() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);
        Person linux = null;
        try {
            linux = (Person) context.getBean("Linux");
        } catch (BeansException e) {
            log.error("Linux环境Bean未注册，获取失败，{}",e.getMessage());
        }
        log.info("测试@Conditional注解，获取Linux环境下Bean，{}",linux);

        Person windows = null;
        try {
            windows = (Person) context.getBean("Windows");
        } catch (BeansException e) {
            log.error("Windows环境Bean未注册，获取失败，{}",e.getMessage());
        }
        log.info("测试@Conditional注解，获取Windows环境下Bean，{}",windows);
    }
}
