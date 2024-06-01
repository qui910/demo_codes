package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

/**
 * Spring获取Bean的方法四：通过继承ApplicationObjectSupport
 * <p>
 *     此种方式依旧是先获得ApplicationContext容器，然后从中获取Bean对象，只不过是基于继承ApplicationObjectSupport类实现的。
 * </p>
 * @version 1.0
 * @date 2023-08-10 10:41
 * @since 1.8
 **/
@Component
public class SpringContextUtil4 extends ApplicationObjectSupport {

    public <T> T getBean(String beanName) {
        ApplicationContext ac = getApplicationContext();
        if(ac == null){
            return null;
        }
        return (T) ac.getBean(beanName);
    }
}
