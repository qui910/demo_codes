package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3;

import lombok.extern.slf4j.Slf4j;
import org.example.designPatterns.DesignPatternsDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 09:35
 * @since 1.8
 **/
//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(classes = DesignPatternsDemoApplication.class)
// 加了@WebAppConfiguration注解，则表示告诉Spring该集成测试加载的ApplicationContext应该是WebApplicationContext
@WebAppConfiguration
@Slf4j
public class FactoryBeanTest {

    @Test
    public void testGetBookFactoryBean() {
        AnnotationConfigApplicationContext annotationContext =
                new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
        // 获取工厂类的bean
        Object bookFactoryBean = annotationContext.getBean("bookFactoryBean");
        // class org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3.Book
        log.info("1:{}",bookFactoryBean.getClass());

        //获取FactoryBean 本身 加 &符号
        Object bookFactoryBean1 = annotationContext.getBean("&bookFactoryBean");
        // class org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3.BookFactoryBean
        log.info("2:{}",bookFactoryBean1.getClass());
    }
}
