package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo1;

import org.junit.Test;

/**
 * 简单工厂模式测试类
 * @version 1.0
 * @date 2023-08-09 09:50
 * @since 1.8
 **/
public class FactoryTest {

    @Test
    public void testFactory() {
        Product product;
        String type = "A";
        product = Factory.getProduct(type);
        product.methodSame();
        product.methodDiff();
        type = "B";
        product = Factory.getProduct(type);
        product.methodSame();
        product.methodDiff();
    }
}
