package com.example.javase.io.nio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * NIO实现多人聊天室,功能
 * （1）基于NIO模型
 * （2）支持多人同时在线
 * （3）每个用户的发言都被转发给其他用户
 * <p>
 * NIO服务器 非阻塞 单线程（指selector.select()）
 */
public class NIOChatServer {

    public static final int DEFAULT_PORT = 9999;
    public static final String QUIT = "quit";
    public static final int BUFFER = 1024;

    private ServerSocketChannel server;
    private Selector selector;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Charset charset = Charset.forName("UTF-8");
    private int port;

    public NIOChatServer(int port) {
        this.port = port;
    }

    public NIOChatServer() {
        this(DEFAULT_PORT);
    }

    /**
     * 启动服务器
     */
    public void start() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("启动服务器，监听端口:" + port + "...");

            while (true) {
                // 阻塞，直到发生OP_ACCEPT时间
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    // 处理被触发的事件
                    handlers(key);
                }
                // 必须清除事件，放置下次又被触发
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
        // ACCEP事件 -- 和客户端建立了连接
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端[" + client.socket().getPort() + "]已经连接");
        }
        // READ事件 -- 客户端发送了消息
        else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            String fwdMsg = receive(client);
            if (fwdMsg.isEmpty()) {
                // 消息为空，客户端异常，取消selector上监听当前通道的事件
                key.cancel();
                // selector上的事件已经更新，通知selector重新唤醒自身上监听的其他事件
                // 这里是单线程模式（selector.select();返回后才处理），所以wakeup的作用不高
                selector.wakeup();
            } else {
                forwardMessage(client, fwdMsg);

                // 检查用户是否退出
                if (readyToQuit(fwdMsg)) {
                    key.cancel();
                    selector.wakeup();
                    System.out.println("客户端[" + client.socket().getPort() + "]已经端口连接");
                }
            }
        }
    }

    private void forwardMessage(SocketChannel client, String fwdMsg) throws IOException {
        for (SelectionKey key : selector.keys()) {
            Channel connectClient = key.channel();
            // 如果通道是服务器，则不转发消息
            if (connectClient instanceof ServerSocketChannel) {
                continue;
            }
            // 保证当前key可用 且不是自己的key
            if (key.isValid() && !client.equals(connectClient)) {
                wBuffer.clear();
                wBuffer.put(charset.encode("客户端[" + client.socket().getPort() + "]:" + fwdMsg));
                wBuffer.flip();
                while (wBuffer.hasRemaining()) {
                    ((SocketChannel) connectClient).write(wBuffer);
                }
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        // 清空残留信息，因为是类变量
        rBuffer.clear();
        while (client.read(rBuffer) > 0) {
            rBuffer.flip();
        }
        return String.valueOf(charset.decode(rBuffer));
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

    public static void main(String[] args) {
        NIOChatServer chatServer = new NIOChatServer();
        chatServer.start();
    }
}

