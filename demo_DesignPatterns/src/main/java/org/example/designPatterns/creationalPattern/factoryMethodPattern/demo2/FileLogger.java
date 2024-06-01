package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件日志记录器：具体产品
 * @version 1.0
 * @date 2023-08-10 15:31
 * @since 1.8
 **/
@Slf4j
public class FileLogger implements Logger {
    @Override
    public void writeLog() {
        log.info("文件日志记录。");
    }
}
