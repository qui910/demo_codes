package com.example.spring.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author pang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Luban {
    String value();
}
