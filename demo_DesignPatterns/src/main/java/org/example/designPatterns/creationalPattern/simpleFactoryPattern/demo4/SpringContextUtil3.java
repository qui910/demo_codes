package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Spring获取Bean的方法三：启动获取ApplicationContext
 * <p>
 * 在项目启动时先获取ApplicationContext对象，然后将其存储在一个地方，以便后续用到时进行使用。
 * 这里提供两种场景的获取：
 *
 * <li>基于xml配置bean的形式，适用于比较古老的项目，已经很少使用了；</li>
 * <li>基于SpringBoot启动时获取ApplicationContext对象；</li>
 *
 * 两种方式都是在启动Spring项目时，直接获取到ApplicationContext的引用，然后将其存储到工具类当中。
 * 在使用时，则从工具类中获取ApplicationContext容器，进而从中获得Bean对象。
 * </p>
 *
 * @version 1.0
 * @date 2023-08-10 09:07
 * @since 1.8
 **/
public class SpringContextUtil3 {

    /**
     * 基于xml的形式实现
     * <p>
     *     这里等于直接初始化容器，并且获得容器的引用。这种方式适用于采用Spring框架的独立应用程序，需要程序通过配置文件手工初始化Spring的情况。
     *     目前大多数Spring项目已经不再采用xml配置，很少使用了。
     * </p>
     */
    @Deprecated
    public Object getBean1(String beanName) {
        // 其中applicationContext.xml 为配置容器的xml，不过现在一般很少使用了
        ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");
        return null;
    }


    private static ApplicationContext ac;
    /**
     * 基于SpringBoot启动实现
     * <p>
     *  需要在Springboot启动类中，添加注入ApplicationContext。eg:
     *   SpringContextUtil.setAc(ac);
     * </p>
     */
    public static <T>  T getBean(String beanName, Class<T> clazz) {
        T bean = ac.getBean(beanName, clazz);
        return bean;
    }

    public static void setAc(ApplicationContext applicationContext){
        ac = applicationContext;
    }
}
