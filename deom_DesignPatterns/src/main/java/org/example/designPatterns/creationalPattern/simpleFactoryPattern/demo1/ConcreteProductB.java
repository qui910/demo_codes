package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * 典型的具体产品类
 * @version 1.0
 * @date 2023-08-09 09:30
 * @since 1.8
 **/
@Slf4j
public class ConcreteProductB extends Product {
    /**
     * 实现业务方法
     */
    @Override
    public void methodDiff() {
        log.info("业务方法的实现B");
    }
}
