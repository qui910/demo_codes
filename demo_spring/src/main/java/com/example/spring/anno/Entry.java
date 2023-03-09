package com.example.spring.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pang
 */
@Target({ElementType.TYPE}) //表明注解使用位置是 类上
@Retention(RetentionPolicy.RUNTIME) //表明在运行时有效
public @interface Entry {
    String value() default "";
}
