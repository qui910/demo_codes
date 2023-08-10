package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * 通过BeanFactoryAware获取
 *
 * 在之前的方式中，XmlBeanFactory已经被废弃，但可以通过其他方式来获得BeanFactory，
 * 然后再从BeanFactory中获得指定的Bean。获取BeanFactory实例最简单的方式就是实现BeanFactoryAware接口。
 *
 * BeanFactoryAware属于org.springframework.beans.factory.Aware根标记接口，
 * 使用setter注入来在应用程序上下文启动期间获取对象。Aware接口是回调，监听器和观察者设计模式的混合，
 * 它表示Bean有资格通过回调方式被Spring容器通知。
 * @version 1.0
 * @date 2023-08-10 08:44
 * @since 1.8
 **/
@Component
public class SpringContextUtil2 implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    /**
     * 重写 BeanFactoryAware 接口的方法
     * @param beanFactory ：参数赋值给本地属性之后即可使用 BeanFactory
     * @throws BeansException BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextUtil2.beanFactory = beanFactory;
    }
    /**
     * 根据名称获取容器中的对象实例
     * @param beanName ：注入的实例必须已经存在容器中，否则抛异常：NoSuchBeanDefinitionException
     * @return Object
     */
    public static Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }
    /**
     * 根据 class 获取容器中的对象实例
     * @param requiredType ：被注入的必须已经存在容器中，否则抛异常：NoSuchBeanDefinitionException
     * @param <T> Class
     * @return 对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }
    /**
     * 判断 spring 容器中是否包含指定名称的对象
     * @param beanName bean名称
     * @return 是否存在
     */
    public static boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    //其它需求皆可参考 BeanFactory 接口和它的实现类
}
