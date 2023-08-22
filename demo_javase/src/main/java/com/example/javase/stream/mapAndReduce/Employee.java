package com.example.javase.stream.mapAndReduce;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 15:46
 * @since 1.8
 **/
public class Employee {
    private final int id;
    private final String name;
    private final int age;

    public Employee(int id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }
}
