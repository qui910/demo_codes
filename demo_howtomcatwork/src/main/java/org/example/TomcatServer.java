package org.example;

import org.example.ex01.HttpServer;

public class TomcatServer {

    public static void main(String[] args) {
        // ex01
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }
}
