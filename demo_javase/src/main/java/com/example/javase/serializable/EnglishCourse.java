package com.example.javase.serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 英语课程
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:56
 * @since 1.8
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EnglishCourse extends Course {

    private int scope;

    public EnglishCourse(String name, int scope) {
        super(name);
        this.scope = scope;
    }
}
