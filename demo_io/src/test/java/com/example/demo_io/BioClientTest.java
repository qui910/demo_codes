package com.example.demo_io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * BIO客户端
 * @author pang
 * @version 1.0
 * @date 2024-07-08 13:39
 * @since 1.8
 **/
@Slf4j
public class BioClientTest {

    @Test
    void sendAndReceiveMessage() {
        String serverAddress = "localhost"; // 服务器地址
        int serverPort = 1083; // 服务器端口

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送消息到服务器
            out.println("Hello from the client!你好");
            log.info("send hello");
            TimeUnit.SECONDS.sleep(60);
            out.println("over");
            log.info("send over");

            // 从服务器接收响应
            String response = in.readLine();
            log.info("Received response: " + response);

        } catch (UnknownHostException e) {
            log.error("Don't know about host: " + serverAddress);
        } catch (IOException e) {
            log.error("Couldn't get I/O for the connection to: " + serverAddress);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
    }
}
