package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 09:28
 * @since 1.8
 **/
@Data
@AllArgsConstructor
@ToString
public class Book {
    private String bookName;
    private String bookType;
    private double price;
}