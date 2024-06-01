package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库日志记录器：具体产品
 * @version 1.0
 * @date 2023-08-10 15:17
 * @since 1.8
 **/
@Slf4j
public class DatabaseLogger implements Logger {
    @Override
    public void writeLog() {
        log.info("数据库日志记录。");
    }
}
