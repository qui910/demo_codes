package com.example.javase.io.bio.test1;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

/**
 * BIO模式的SocketClient
 * 注意：这里不能使用Junit @Test注释，只能使用main方法，否则阻塞无法从Console获取输入
 * @version 1.0
 * @date 2023-07-26 10:55
 * @since 1.8
 **/
public class BIOClientBase {

    public static final String QUIT = "quit";

    public static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    public static final int DEFAULT_SERVER_PORT = 9999;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            // 创建socket
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
            System.out.println("客户端[" + socket + "]与服务器建立连接。");
            // 创建IO流
            System.out.println("请输入信息：");
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            // 等待用户输入信息
            BufferedReader consoleReader = new BufferedReader(
                    new InputStreamReader(System.in));
            // 一直发送消息，直到从屏幕接收到quit，才退出
            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                // 发送消息给服务器
                // 添加换行符，以便服务端readline()可以区分
                writer.write(userInput + "\n");
                writer.flush();

                // 读取服务器返回消息
                System.out.println(reader.readLine());

                // 查看用户是否退出
                if (QUIT.equals(userInput)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //这里关闭writer的话，会自动关闭它封装的OutputStreamWriter 和socket
            if (writer != null) {
                try {
                    writer.close();
                    System.out.println("关闭client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
