package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Spring获取Bean的方法一：通过BeanFactory获取
 * <p>
 *  基于xml配置文件的时代，可以通过如下方式获得BeanFactory，再通过BeanFactory来获得对应的Bean。
 * </p>
 * @version 1.0
 * @date 2023-08-09 14:50
 * @since 1.8
 **/
@Component
public class SpringContextUtil1 {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 鉴于xml形式配置文件已经被基于注解形式所替代，同时XmlBeanFactory也被标注为废弃。此种方式不推荐使用。
     * 其实，不推荐的理由还有一个，在上面已经提到，尽量不要使用BeanFactory，而应该使用ApplicationContext。
     * @return
     */
    @Deprecated
    public Object getBean1(String beanName) {
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
        return beanFactory.getBean(beanName);
    }

    /**
     * 另一种获取 BeanFactory 对象的方式是注入 ApplicationContext 对象，并使用该对象获取 BeanFactory。
     * 在Spring Boot中，您可以使用@Autowired注解将ApplicationContext 对象注入到您的类中，并使用getBeanFactory方法获取BeanFactory对象。
     * @param beanName
     * @return
     */
    public Object getBean2(String beanName) {
        BeanFactory beanFactory = ((GenericWebApplicationContext) applicationContext).getBeanFactory();
        return beanFactory.getBean(beanName);
    }
}
