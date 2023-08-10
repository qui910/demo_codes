package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

/**
 * 文件日志记录器工厂类：具体工厂
 * @version 1.0
 * @date 2023-08-10 15:35
 * @since 1.8
 **/
public class FileLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger() {
        //创建文件日志记录器对象
        Logger logger = new FileLogger();
        //创建文件，代码省略
        return logger;
    }
}
