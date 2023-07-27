package com.example.javase.io.bio.test1;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO模式的SocketServer
 * 注意：这里不能使用Junit @Test注释，只能使用main方法
 * @version 1.0
 * @date 2023-07-26 11:00
 * @since 1.8
 **/
public class BIOServerBase {

    private static final int DEFAULT_PORT = 9999;

    /**
     * 演示单个线程处理客户端连接
     */
    public static void main(String[] args) {
        ServerSocket socketServer = null;
        try {
            // bind绑定监听端口
            socketServer = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，监听端口" + DEFAULT_PORT);

            while (true) {
                // 等待客户端连接
                Socket socket = socketServer.accept();
                System.out.println("客户端[" + socket.toString() + "]已连接");

                // BIO处理数据
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));

                // 读取客户端消息，发送的消息中有行分隔符时，才能用readLine()接收。
                // 不然就都是一行了
                String msg = null;
                while ((msg = reader.readLine()) != null) {
                    System.out.println("客户端[" + socket.toString() + "]:" + msg);

                    // 回复消息
                    writer.write("服务器：" + msg + "\n");
                    writer.flush();

                    //查看客户端是否退出
                    if (BIOClientBase.QUIT.equals(msg)) {
                        System.out.println("客户端[" + socket.toString() + "]已退出");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketServer != null) {
                try {
                    socketServer.close();
                    System.out.println("关闭server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
