package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Logger日志记录工厂方法模式测试类
 * @author pangruidong
 * @version 1.0
 * @date 2023-08-10 15:36
 * @since 1.8
 **/
@Slf4j
public class FactoryMethodTest {

    @Test
    public void testGetLog() {
        LoggerFactory factory;
        Logger logger;
        factory = new FileLoggerFactory(); //可引入配置文件实现
        logger = factory.createLogger();
        logger.writeLog();
    }

    /**
     * 引入配置文件实现
     */
    @Test
    public void testGetLogFromXMl() {
        LoggerFactory factory;
        Logger logger;
        factory = (LoggerFactory)XMLUtil.getBean(this.getClass().getResource("").getPath()); //getBean()
        // 的返回类型为Object，需要进行强制类型转换
        logger = factory.createLogger();
        logger.writeLog();
    }

    @Test
    public void filePath() throws IOException {
        log.info("1 获取当前文件所在的路径:{}",this.getClass().getResource("").getPath());
        log.info("2 获取再target下classpath路径:{}",this.getClass().getResource("/").getPath());
        log.info("3 也是获取classpath的绝对路径:{}",Thread.currentThread().getContextClassLoader().getResource("").getPath());
        log.info("4 也是获取classpath的绝对路径:{}",this.getClass().getClassLoader().getResource("").getPath());
        log.info("5 也是获取classpath的绝对路径:{}",ClassLoader.getSystemResource("").getPath());
        log.info("6 获取当前项目路径（此方法与7效果相同，但是可以将路径转为标准形式，会处理“.”和“..”）： {}",new File("").getCanonicalPath());
        log.info("7 获取项目绝对路径（不会处理“.”和“..”）:{}",new File("").getAbsolutePath());
    }
}
