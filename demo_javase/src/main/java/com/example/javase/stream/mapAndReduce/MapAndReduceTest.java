package com.example.javase.stream.mapAndReduce;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-21 16:07
 * @since 1.8
 **/
@Slf4j
public class MapAndReduceTest {

    private static List<Employee> peoples = new ArrayList<>();

    static {
        peoples.add(new Employee(101, "Victor", 23));
        peoples.add(new Employee(102, "Rick", 21));
        peoples.add(new Employee(103, "Sam", 25));
        peoples.add(new Employee(104, "John", 27));
        peoples.add(new Employee(105, "Grover", 23));
        peoples.add(new Employee(106, "Adam", 22));
        peoples.add(new Employee(107, "Samy", 224));
        peoples.add(new Employee(108, "Duke", 29));
    }

    /**
     * 常用写法
     */
    @Test
    public void testNormal() {
        long startTime = System.currentTimeMillis();
        int totalEmployee = peoples.size();
        double sum = 0; for(Employee e : peoples){
            sum += e.getAge();
        }

        double average = sum/totalEmployee;
        log.info("average={}",average);
        long endTime = System.currentTimeMillis();
        log.info("normal method cost {}",(endTime-startTime));
    }

    /**
     * 使用Map方式
     */
    @Test
    public void testMapAverage() {
        long startTime = System.currentTimeMillis();
        double average = peoples.stream().mapToInt(Employee::getAge)
                .average()
                .getAsDouble();
        log.info("average={}",average);
        long endTime = System.currentTimeMillis();
        log.info("map method cost {}",(endTime-startTime));
    }

    /**
     * 常用写法
     */
    @Test
    public void testNormalSum() {
        long startTime = System.currentTimeMillis();
        double sum = 0; for(Employee e : peoples){
            sum += e.getAge();
        }

        log.info("sum={}",sum);
        long endTime = System.currentTimeMillis();
        log.info("normal method cost {}",(endTime-startTime));
    }

    @Test
    public void testMapSum() {
        long startTime = System.currentTimeMillis();
        double sum = peoples.stream().mapToInt(Employee::getAge)
                .sum();
        log.info("sum={}",sum);
        long endTime = System.currentTimeMillis();
        log.info("map method cost {}",(endTime-startTime));
    }

    @Test
    public void testReduceSum() {
        long startTime = System.currentTimeMillis();
        double sum = peoples.stream().map(Employee::getAge).reduce(0, Integer::sum);
        log.info("sum={}",sum);
        long endTime = System.currentTimeMillis();
        log.info("map method cost {}",(endTime-startTime));
    }

    @Test
    public void testReduceSum1() {
        long startTime = System.currentTimeMillis();
        List<Integer> ages = Arrays.asList(25, 30, 45, 28, 32);
        int computedAges = ages.parallelStream().reduce(0, (a, b) -> a + b, Integer::sum);
        log.info("computedAges={}",computedAges);
        long endTime = System.currentTimeMillis();
        log.info("map method cost {}",(endTime-startTime));
    }
}
