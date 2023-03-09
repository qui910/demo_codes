package com.example.spring.condition;

import org.springframework.context.annotation.*;

/**
 * 根据不同的操作系统加载不同的对象
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:01
 * @since 1.8
 **/
@Configuration
@ComponentScan("com.example.spring.condition")
@Scope("prototype")
public class ConditionConfig {

    @Bean(name = "Windows")
    @Conditional({WindowsCondition.class})
    public Person person1(){
        return new Person("Windows",62);
    }

    @Bean("Linux")
    @Conditional({LinuxCondition.class})
    public Person person2(){
        return new Person("Linux",48);
    }
}
