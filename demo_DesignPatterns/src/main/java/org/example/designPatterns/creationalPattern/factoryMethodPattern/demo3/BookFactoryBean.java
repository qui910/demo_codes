package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 09:29
 * @since 1.8
 **/
public class BookFactoryBean implements FactoryBean<Book> {
    public BookFactoryBean factoryBeanVO() {
        return new BookFactoryBean();
    }

    @Override
    public Book getObject() throws Exception {
        return new Book("红楼梦", "中国名著", 88);
    }

    @Override
    public Class<?> getObjectType() {
        return Book.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}