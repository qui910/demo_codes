package com.example.spring.ioc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试纯注解方式的Spring工程启动及IoC注入
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:32
 * @since 1.8
 **/
@Slf4j
public class IocBeanTest {

    @Test
    public void iocByAnnotation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IoCService service = (IoCService) context.getBean("iocServiceImpl");
        service.query();
    }
}
