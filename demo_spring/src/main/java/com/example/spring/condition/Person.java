package com.example.spring.condition;

import lombok.Data;

/**
 * 测试Conditional的对象
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:07
 * @since 1.8
 **/
@Data
public class Person {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}
