package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品类1，继承抽象产品类
 * @version 1.0
 * @date 2023-08-10 13:48
 * @since 1.8
 **/
@Slf4j
public class Product1 extends Product{
    /**
     * 实现抽象产品类的抽象方法
     */
    @Override
    public void method2() {
        log.info("Product1 抽象方法");
    }
}
