package com.example.spring.condition;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Windows系统环境
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:04
 * @since 1.8
 **/
public class WindowsCondition implements Condition {

    public static final String WINDOWS_OS_NAME = "Windows";

    /**
     * @param conditionContext:判断条件能使用的上下文环境
     * @param annotatedTypeMetadata:注解所在位置的注释信息
     * */
    @Override
    public boolean matches(ConditionContext conditionContext, @NotNull AnnotatedTypeMetadata annotatedTypeMetadata) {
        //获取ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        //获取类加载器
        ClassLoader classLoader = conditionContext.getClassLoader();
        //获取当前环境信息
        Environment environment = conditionContext.getEnvironment();
        //获取bean定义的注册类
        BeanDefinitionRegistry registry = conditionContext.getRegistry();

        //获得当前系统名
        String property = environment.getProperty("os.name");
        //包含Windows则说明是windows系统，返回true
        assert property != null;
        return property.contains(WINDOWS_OS_NAME);
    }

}
