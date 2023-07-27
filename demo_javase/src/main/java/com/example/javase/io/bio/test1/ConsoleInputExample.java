package com.example.javase.io.bio.test1;

/**
 * 读取控制台输入
 * @version 1.0
 * @date 2023-07-27 08:37
 * @since 1.8
 **/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleInputExample {
    // 使用Scanner类：
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("请输入您的姓名：");
//        String name = scanner.nextLine();
//
//        System.out.print("请输入您的年龄：");
//        int age = scanner.nextInt();
//
//        System.out.println("您的姓名是：" + name);
//        System.out.println("您的年龄是：" + age);
//
//        scanner.close(); // 不要忘记在使用完Scanner后关闭它
//    }

    // 使用BufferedReader类
public static void main(String[] args) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    try {
        System.out.print("请输入您的姓名：");
        String name = reader.readLine();

        System.out.print("请输入您的年龄：");
        int age = Integer.parseInt(reader.readLine());

        System.out.println("您的姓名是：" + name);
        System.out.println("您的年龄是：" + age);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            reader.close(); // 不要忘记在使用完BufferedReader后关闭它
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}
