package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * 典型的工厂类
 * @version 1.0
 * @date 2023-08-09 09:46
 * @since 1.8
 **/
@Slf4j
public class Factory {
    /**
     * 静态工厂方法
     * @param arg 类型参数
     * @return 具体实例
     */
    public static Product getProduct(String arg) {
        Product product = null;
        if (arg.equalsIgnoreCase("A")) {
            product = new ConcreteProductA();
            log.info("初始化设置productA");
        }
        else if (arg.equalsIgnoreCase("B")) {
            product = new ConcreteProductB();
            log.info("初始化设置productB");
        }
        return product;
    }
}
