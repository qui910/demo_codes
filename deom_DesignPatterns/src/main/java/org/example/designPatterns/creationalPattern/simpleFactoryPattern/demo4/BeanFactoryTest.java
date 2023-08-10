package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.example.designPatterns.DesignPatternsDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.http.HttpServletRequest;

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
// 加了@WebAppConfiguration注解，则表示告诉Spring该集成测试加载的ApplicationContext应该是WebApplicationContext
@WebAppConfiguration
public class BeanFactoryTest {

    @Autowired
    private SpringContextUtil1 springContextUtil1;

    @Autowired
    private SpringContextUtil4 springContextUtil4;

    @Autowired
    private SpringContextUtil5 springContextUtil5;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SpringContextUtil8 springContextUtil8;


    /**
     * 通过BeanFactory获取 - xml获取BeanFactory 跳过
     */
    @Test
    public void testGetBean11() {

    }

    /**
     * 通过BeanFactory获取 - 通过注入 ApplicationContext 对象
     */
    @Test
    public void testGetBean12() {
        ShippingService shippingService = (ShippingService) springContextUtil1.getBean2("postShip");
        shippingService.ship("111111111111111");
    }

    /**
     * 通过BeanFactoryAware获取
     */
    @Test
    public void testGetBean2() {
        ShippingService shippingService = (ShippingService) SpringContextUtil2.getBean("tCatShip");
        shippingService.ship("22222222222222222");
    }

    /**
     * 启动获取ApplicationContext
     * <p>
     *  此处JUNIT4无法真正加载 DesignPatternsDemoApplication，所以ac无法被注入。
     *  此处无法测试
     * </p>
     */
    @Test
    public void testGetBean3() {
        ShippingService shippingService =SpringContextUtil3.getBean("tCatShip",ShippingService.class);
        shippingService.ship("33333333333333333333");
    }

    /**
     * 通过继承ApplicationObjectSupport
     * <p>
     *     注意，这里的SpringContextUtil类需要实例化。
     * </p>
     */
    @Test
    public void testGetBean4() {
        ShippingService shippingService =springContextUtil4.getBean("tCatShip");
        shippingService.ship("444444444444444444");
    }

    /**
     * 通过继承ApplicationObjectSupport
     * <p>
     *     注意，这里的SpringContextUtil类需要实例化。
     *
     *     对照基于ApplicationObjectSupport的实现，除了继承对象不同外，没有其他区别，都是基于getApplicationContext方法来获取。
     * </p>
     */
    @Test
    public void testGetBean5() {
        ShippingService shippingService =springContextUtil5.getBean("tCatShip");
        shippingService.ship("5555555555555555555");
    }

    /**
     * 通过WebApplicationContextUtils
     */
    @Test
    public void testgetBean61() {
        ShippingService shippingService =SpringContextUtil6.getBean1(request.getServletContext(),"tCatShip",
                ShippingService.class);
        shippingService.ship("666666666666661111");
    }

    /**
     * 通过RequestContextUtils
     */
    @Test
    public void testgetBean62() {
        ShippingService shippingService =SpringContextUtil6.getBean2(request,"tCatShip",
                ShippingService.class);
        shippingService.ship("6666666666666666222");
    }

    /**
     * 通过ApplicationContextAware
     */
    @Test
    public void testgetBean7() {
        ShippingService shippingService = SpringContextUtil7.getBean("tCatShip");
        shippingService.ship("77777777777777777777");
    }

    /**
     * 通过ContextLoader
     */
    @Test
    public void testGetBean8() {
        ShippingService shippingService = (ShippingService) springContextUtil8.getBean("tCatShip");
        shippingService.ship("888888888888888888");
    }

    /**
     * 通过BeanFactoryPostProcessor
     */
    @Test
    public void testGetBean9() {
        ShippingService shippingService = SpringContextUtil9.getBean("tCatShip");
        shippingService.ship("9999999999999999");
    }
}
