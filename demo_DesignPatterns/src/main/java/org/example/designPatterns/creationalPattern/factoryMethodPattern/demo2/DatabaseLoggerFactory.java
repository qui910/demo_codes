package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库日志记录器工厂类：具体工厂
 * @version 1.0
 * @date 2023-08-10 15:35
 * @since 1.8
 **/
@Slf4j
public class DatabaseLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger() {
        //连接数据库，代码省略
        //创建数据库日志记录器对象
        Logger logger = new DatabaseLogger();
        //初始化数据库日志记录器，代码省略
        return logger;
    }
}
