package com.example.javase.inner;

import lombok.Data;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:38
 * @since 1.8
 **/
@Data
public class Person {

    private String name;

    public void doWork() {
        new Name().work();
    }

    protected class Name{
        public void work() {
            System.out.println("Person.Name work");
        }
    }
}
