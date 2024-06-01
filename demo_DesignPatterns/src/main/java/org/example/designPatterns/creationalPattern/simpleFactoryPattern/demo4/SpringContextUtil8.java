package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring获取Bean的方法八：通过ContextLoader
 * <p>
 *     使用ContextLoader提供的getCurrentWebApplicationContext方法，也是常用的获取WebApplicationContext的一种方法。
 *
 *     该方法常见于SpringMVC实现的Web项目中。该方式是一种不依赖于Servlet，不需要注入的方式。
 *     但是需要注意一点，在服务器启动时和Spring容器初始化时，不能通过该方法获取Spring容器。
 *
 *     说明：目前通过这种方式获取上下文为null，从代码可以看出，上下文是通过currentContextPerThread.get(ccl) 来获取的，
 *     而currentContextPerThread缓存是通过方法contextInitialized(ServletContextEvent event) 来初始化的，
 *     至于为何获取为空，参考：https://www.cnblogs.com/xysn/p/14863896.html
 * </p>
 * @version 1.0
 * @date 2023-08-10 13:04
 * @since 1.8
 **/
@Component
public class SpringContextUtil8 {

    /**
     * 直接调用：ContextLoader.getCurrentWebApplicationContext()，
     * 或者 ContextLoaderListener.getCurrentWebApplicationContext() 其实都是调用同一段代码
     */
    public Object getBean(String beanName) {
        WebApplicationContext applicationContext =  ContextLoaderListener.getCurrentWebApplicationContext();
        return applicationContext.getBean(beanName);
    }
}
