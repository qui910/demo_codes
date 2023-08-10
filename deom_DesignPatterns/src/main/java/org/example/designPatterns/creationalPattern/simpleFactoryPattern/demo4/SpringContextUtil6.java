package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Spring获取Bean的方法六：通过WebApplicationContextUtils等工具类
 * <p>
 *     Spring提供了工具类WebApplicationContextUtils，通过该类可获取WebApplicationContext对象。
 *
 *     这个方法很常见于SpringMVC构建的Web项目中，适用于Web项目的B/S结构。
 *
 *     在controller中传入request可以直接使用入参的request
 *     WebApplicationContext wc = RequestContextUtils.findWebApplicationContext(request);
 *
 *     在service中或者其他后端服务中获取，可以先获取request
 *     HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
 * </p>
 * @version 1.0
 * @date 2023-08-10 10:55
 * @since 1.8
 **/
public class SpringContextUtil6 {
    public static <T> T getBean1(ServletContext request, String name, Class<T> clazz){
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request);
        // 或者
        WebApplicationContext webApplicationContext1 = WebApplicationContextUtils.getWebApplicationContext(request);

        // webApplicationContext1.getBean(name, clazz)
        T bean = webApplicationContext.getBean(name, clazz);
        return bean;
    }

    public static <T> T getBean2(HttpServletRequest request, String name, Class<T> clazz){
        WebApplicationContext wc = RequestContextUtils.findWebApplicationContext(request);
        T bean = wc.getBean(name, clazz);
        return bean;
    }
}
