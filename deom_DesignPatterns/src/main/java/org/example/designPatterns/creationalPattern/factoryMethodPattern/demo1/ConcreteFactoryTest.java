package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * ConcreteFactory测试类
 * @version 1.0
 * @date 2023-08-10 14:46
 * @since 1.8
 **/
@Slf4j
public class ConcreteFactoryTest {

    @Test
    public void testCreate() {
        //创建具体工厂类
        ConcreteFactory factory = new ConcreteFactory();
        //调用工厂方法获取产品类1的实例
        Product1 product1 = factory.create(Product1.class);
        log.info("CREATE 对象 {}",product1);
        product1.method1();
        product1.method2();
    }
}
