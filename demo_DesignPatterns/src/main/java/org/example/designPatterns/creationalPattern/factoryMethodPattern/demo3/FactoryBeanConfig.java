package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 09:33
 * @since 1.8
 **/
@Configuration
public class FactoryBeanConfig  {
    @Bean
    public BookFactoryBean bookFactoryBean()
    {
        return new BookFactoryBean();
    }
}