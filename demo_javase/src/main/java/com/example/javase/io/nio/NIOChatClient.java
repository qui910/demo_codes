package com.example.javase.io.nio;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * NIO实现多人聊天室,功能
 * （1）基于NIO模型
 * （2）支持多人同时在线
 * （3）每个用户的发言都被转发给其他用户
 * <p>
 * NIO客户端 非阻塞
 */
public class NIOChatClient {

    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String QUIT = "quit";
    public static final int BUFFER = 1024;

    private SocketChannel client;
    private Selector selector;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");
    private int port;
    private String host;

    public NIOChatClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public NIOChatClient() {
        this(DEFAULT_PORT, DEFAULT_HOST);
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

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动客户端
     */
    public void start() {
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);

            selector = Selector.open();
            //注册 连接事件监听
            client.register(selector, SelectionKey.OP_CONNECT);
            client.connect(new InetSocketAddress(host, port));

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    handlers(key);
                }
                selectionKeys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 只关闭selector，就可以同时关闭selector上注册的通道
            close(selector);
        }
    }

    private void handlers(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        // CONNECT事件 --连接就绪事件
        if (key.isConnectable()) {
            // 判断连接是否就绪，如果返回false表明连接正在进行，需要等待。
            if (client.isConnectionPending()) {
                client.finishConnect();
                System.out.println("连接就绪");
                // 处理用户的输入
                new Thread(new UserInputHandler(this)).start();
            }
            client.register(selector, SelectionKey.OP_READ);
        }
        // READ事件 --服务器转发消息
        else if (key.isReadable()) {
            String msg = receive(client);
            if (msg.isEmpty()) {
                //服务器异常,则客户端退出
                close(selector);
            } else {
                System.out.println(msg);
            }
        }
    }

    public void send(String msg) throws IOException {
        if(msg.isEmpty()){
            return;
        }else{
            wBuffer.clear();
            wBuffer.put(charset.encode(msg));
            wBuffer.flip();
            while(wBuffer.hasRemaining()){
                client.write(wBuffer);
            }
            //检查用户是否准备退出
            if(readyToQuit(msg)){
                close(selector);
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while (client.read(rBuffer) > 0) {
            rBuffer.flip();
        }
        return String.valueOf(charset.decode(rBuffer));
    }


    private static class UserInputHandler implements Runnable{

        private NIOChatClient chatclient;

        public UserInputHandler(NIOChatClient chatClient){
            this.chatclient = chatClient;
        }

        @Override
        public void run() {
            try {
                //等待用户输入的消息
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

                while(true){
                    String input = consoleReader.readLine();
                    //向服务器发送消息
                    chatclient.send(input);

                    //检查用户是否准备退出
                    if(chatclient.readyToQuit(input)){
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        NIOChatClient chatClient = new NIOChatClient();
        chatClient.start();
    }
}
