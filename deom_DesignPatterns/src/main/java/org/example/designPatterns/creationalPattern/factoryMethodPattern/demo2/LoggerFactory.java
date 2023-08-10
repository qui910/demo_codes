package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

/**
 * 日志记录器工厂接口：抽象工厂
 * @version 1.0
 * @date 2023-08-10 15:34
 * @since 1.8
 **/
public interface LoggerFactory {
    Logger createLogger();
}
