package com.example.javase.serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:57
 * @since 1.8
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    private String name;
}
