package com.example.springbootdemo.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author pang
 * @version 1.0
 * @date 2023-10-10 14:10
 * @since 1.8
 **/
public class UserEvent<T> extends ApplicationEvent {
    private T data;

    public UserEvent(T source) {
        super(source);
        this.data =source;
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}
