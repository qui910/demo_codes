package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring获取Bean的方法七：通过ApplicationContextAware
 * <p>
 *     通过实现ApplicationContextAware接口，在Spring容器启动时将ApplicationContext注入进去，从而获取ApplicationContext对象，
 *     这种方法也是常见的获取Bean的一种方式，推荐使用。
 *
 *     这种方式与前面通过BeanFactoryAware获得BeanFactory的思路一致。
 * </p>
 * @version 1.0
 * @date 2023-08-10 11:10
 * @since 1.8
 **/
@Component
public class SpringContextUtil7 implements ApplicationContextAware {
    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        T bean = (T) ac.getBean(beanName);
        return bean;
    }
}
