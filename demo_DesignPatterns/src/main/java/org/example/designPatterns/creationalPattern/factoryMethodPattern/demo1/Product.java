package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象产品类
 * @version 1.0
 * @date 2023-08-10 13:47
 * @since 1.8
 **/
@Slf4j
public abstract class Product {
    /**
     * 公共逻辑方法
     */
    public void method1(){
        log.info("公共逻辑方法");
    }

    /**
     * 抽象方法：由子类实现，根据业务逻辑定义多个
     */
    public abstract void method2();
}
