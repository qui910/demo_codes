package com.example.javase.io.bio.test2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO实现多人聊天室,功能
 * （1）基于BIO模型
 * （2）支持多人同时在线
 * （3）每个用户的发言都被转发给其他用户
 * <p>
 * 分单线程和多线程方式
 */
public class BIOChatServer {
    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String QUIT = "quit";

    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Map<Integer, Writer> connectedClients = new HashMap<>();

    /**
     * 新接入客户端，并将客户端加入到连接Map中
     * 多个线程客户端调用，需要线程安全
     *
     * @param socket
     * @throws IOException
     */
    private synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            connectedClients.put(port, writer);
            System.out.println("客户端[" + socket.getInetAddress() + socket.getPort() + "]已连接到服务器");
        }
    }

    /**
     * 关闭客户端，并将客户端从连接Map中清除
     * 多个线程客户端调用，需要线程安全
     *
     * @param socket
     * @throws IOException
     */
    private synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClients.containsKey(port)) {
                // socket是通过writer进行封装的，所以关闭writer就会关闭socket
                connectedClients.get(port).close();
                connectedClients.remove(port);
                System.out.println("客户端[" + socket.getInetAddress() + socket.getPort() + "]已断开连接");
            }
        }
    }

    /**
     * 将客户端socket发生来的消息，转发到其他已连接的所有客户端
     *
     * @param socket
     * @param fwdMsg
     */
    private synchronized void forwardMessage(Socket socket, String fwdMsg) {
        connectedClients.forEach((port, writer) -> {
            if (port != socket.getPort()) {
                try {
                    writer.write(fwdMsg);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 启动服务器
     */
    public void start() {
        try {
            // 绑定监听端口
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，监听端口：" + DEFAULT_PORT);

            while (true) {
                // 等待客户端连接
                Socket socket = serverSocket.accept();
//                // 创建ChatHandler线程
//                new Thread(new ChatHandler(socket,this)).start();

                // 使用线程池
                executorService.execute(new ChatHandler(socket, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public synchronized void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查用户是否退出
     *
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    /**
     * 每个客户端连接，建立一个线程来处理
     */
    private static class ChatHandler implements Runnable {
        private Socket socket;

        private BIOChatServer server;

        public ChatHandler(Socket socket, BIOChatServer server) {
            this.socket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                // 保存新连接用户
                server.addClient(socket);

                //读取用户发送的消息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                while (true) {
                    String msg = reader.readLine();
                    String fwdMsg = "客户端[" + socket.getInetAddress() + socket.getPort() + "]:" + msg;
                    System.out.println(fwdMsg);

                    // 将消息转发给聊天室里在线的其他用户
                    // 添加 \n 方便接收方readline方法处理
                    server.forwardMessage(socket, fwdMsg + "\n");

                    // 检查用户是否准备退出
                    if (server.readyToQuit(msg)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    server.removeClient(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BIOChatServer server = new BIOChatServer();
        server.start();
    }
}