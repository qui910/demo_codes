package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 * Spring获取Bean的方法五：通过继承WebApplicationObjectSupport
 * @version 1.0
 * @date 2023-08-10 10:52
 * @since 1.8
 **/
@Component
public class SpringContextUtil5 extends WebApplicationObjectSupport {
    public <T> T getBean(String beanName) {
        ApplicationContext ac = getApplicationContext();
        if(ac == null){
            return null;
        }
        return (T) ac.getBean(beanName);
    }
}