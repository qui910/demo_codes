package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.example.designPatterns.DesignPatternsDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * BeanFactory测试类
 * @version 1.0
 * @date 2023-08-09 15:03
 * @since 1.8
 **/
//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(classes = DesignPatternsDemoApplication.class)
@WebAppConfiguration
public class BeanFactoryTest {

    @Autowired
    private SpringContextUtil1 springContextUtil1;

    /**
     * 通过BeanFactory获取 - xml获取BeanFactory 跳过
     */
    @Test
    public void testGetBean1() {

    }

    /**
     * 通过BeanFactory获取 - 通过注入 ApplicationContext 对象
     */
    @Test
    public void testGetBean2() {
        ShippingService shippingService = (ShippingService) springContextUtil1.getBean2("postShip");
        shippingService.ship("111111111111111");
    }

    /**
     * 通过BeanFactoryAware获取
     */
    @Test
    public void testGetBean3() {
        ShippingService shippingService = (ShippingService) SpringContextUtil2.getBean("tCatShip");
        shippingService.ship("22222222222222222");
    }
}
