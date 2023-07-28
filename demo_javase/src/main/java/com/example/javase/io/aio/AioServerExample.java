package com.example.javase.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * 创建一个AIO服务器，它将异步地接收客户端的连接请求，并异步地读取和写入数据。
 *
 * 这个AIO服务器示例会异步地接受客户端连接请求，并在客户端连接成功后，异步地读取客户端发送的数据，并异步地将响应写回给客户端。
 * 注意，AIO使用了回调函数（CompletionHandler）来处理异步事件。
 *
 * 请注意，这是一个简单的示例，并没有处理异常、关闭资源等细节。在实际应用中，您需要适当地处理这些情况，以确保服务器的稳定性和可靠性。
 * @version 1.0
 * @date 2023-07-27 10:24
 * @since 1.8
 **/
public class AioServerExample {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        int port = 12345;
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
        serverChannel.bind(hostAddress);
        System.out.println("服务器启动，等待客户端连接...");

        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                // 接受下一个连接
                serverChannel.accept(null, this);

                // 处理当前连接
                handleClient(clientChannel);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println("连接失败: " + exc.getMessage());
            }
        });

        // 让主线程阻塞，保持服务器运行
        Thread.currentThread().join();
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 异步读取客户端数据
        clientChannel.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer bytesRead, Void attachment) {
                if (bytesRead > 0) {
                    buffer.flip();
                    byte[] data = new byte[bytesRead];
                    buffer.get(data);
                    String message = new String(data).trim();
                    System.out.println("收到来自客户端的消息：" + message);

                    // 处理消息（在此处添加您的处理逻辑）

                    // 异步将响应写回给客户端
                    buffer.clear();
                    buffer.put("服务器响应：收到消息".getBytes());
                    buffer.flip();
                    clientChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
                        @Override
                        public void completed(Integer bytesWritten, Void attachment) {
                            // 继续异步读取下一条消息
                            buffer.clear();
                            clientChannel.read(buffer, null, this);
                        }

                        @Override
                        public void failed(Throwable exc, Void attachment) {
                            System.err.println("写入失败: " + exc.getMessage());
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println("读取失败: " + exc.getMessage());
            }
        });
    }
}

