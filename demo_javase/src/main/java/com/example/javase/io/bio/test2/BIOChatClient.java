package com.example.javase.io.bio.test2;

import java.io.*;
import java.net.Socket;

/**
 * BIO实现多人聊天室,功能
 * （1）基于BIO模型
 * （2）支持多人同时在线
 * （3）每个用户的发言都被转发给其他用户
 * <p>
 * 这里因为使用consoleReader.readLine();不能使用JUint调试
 * 只能使用main方法，否则会到consoleReader.readLine();无反应卡死
 */
public class BIOChatClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * 发送消息到服务端
     *
     * @param msg
     * @throws IOException
     */
    private void send(String msg) throws IOException {
        if (!socket.isOutputShutdown()) {
            writer.write(msg + "\n");
            writer.flush();
        }
    }

    private String receive() throws IOException {
        return socket.isInputShutdown() ? null : reader.readLine();
    }

    /**
     * 检查用户是否退出
     *
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return BIOChatServer.QUIT.equals(msg);
    }

    public void close() {
        System.out.println("关闭socket");
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 创建socket
            socket = new Socket(BIOChatServer.DEFAULT_HOST,
                    BIOChatServer.DEFAULT_PORT);

            //创建IO流
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            //处理用户输入，这里需要开启另外的线程
            //因为等待用户输入时阻塞式的，如果不开启额外的线程，会导致读数据被阻塞，无法接受消息
            new Thread(new UserInputHandler(this)).start();

            //读取服务器转发的消息
            String msg = null;
            while ((msg = receive()) != null) {
                System.out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * 用户输入处理线程
     * 单独开启线程是因为读取控制台输入，是会造成阻塞的。
     * 防止阻塞从服务器获取消息。
     */
    private static class UserInputHandler implements Runnable {

        private BIOChatClient chatClient;

        public UserInputHandler(BIOChatClient chatClient) {
            this.chatClient = chatClient;
        }


        @Override
        public void run() {
            try {
                System.out.println("请输入:");
                // 等待用户输入消息
                BufferedReader consoleReader =
                        new BufferedReader(
                                new InputStreamReader(System.in));

                while (true) {
                    //一般从控制台获取输入，是会有回车的，所以这里用readline
                    String input = consoleReader.readLine();
                    // 向服务器发送消息
                    chatClient.send(input);

                    // 检查用户是否准备推出
                    if (chatClient.readyToQuit(input)) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("关闭");
            }
        }
    }

    public static void main(String[] args) {
        BIOChatClient client = new BIOChatClient();
        client.start();
    }
}
