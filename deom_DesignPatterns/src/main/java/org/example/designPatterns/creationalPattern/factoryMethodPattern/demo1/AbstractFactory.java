package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1;

/**
 * 抽象工厂类，必须定义一个工厂方法来自己实现具体的创建逻辑
 * @version 1.0
 * @date 2023-08-10 14:24
 * @since 1.8
 **/
public abstract class AbstractFactory {
    /**
     * 工厂方法，需要子类实现
     * @param cls
     * @param <T>
     * @return
     */
    public abstract <T extends Product> T create(Class<T> cls);
}
