package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo1;

import lombok.extern.slf4j.Slf4j;

/**
 * 典型的抽象产品类代码
 * @version 1.0
 * @date 2023-08-09 09:22
 * @since 1.8
 **/
@Slf4j
public abstract class Product {
    /**
     * 所有产品类的公共业务方法
     */
    public void methodSame() {
        log.info("抽象产品类-公共方法的实现");
    }

    /**
     * 声明抽象业务方法
     */
    public abstract void methodDiff();
}
