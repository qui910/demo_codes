package org.example;

import org.example.ex01.HttpServer01;
import org.example.ex02.HttpServer02;

public class TomcatServer {

    public static void main(String[] args) {
        // ex01
//        HttpServer01 httpServer = new HttpServer01();
//        httpServer.await();
        // ex02
        HttpServer02 httpServer2 = new HttpServer02();
        httpServer2.await();
    }
}
