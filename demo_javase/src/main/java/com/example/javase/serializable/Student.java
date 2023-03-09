package com.example.javase.serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 11:03
 * @since 1.8
 **/
@Data
@AllArgsConstructor
public class Student implements Serializable {
    private String name;

    private int age;

    private Course course;
}
