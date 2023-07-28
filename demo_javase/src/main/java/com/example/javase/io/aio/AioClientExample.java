package com.example.javase.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * 一个简单的AIO客户端示例，它可以连接到AIO服务器并异步地发送和接收数据。
 *
 * 在这个AIO客户端示例中，我们首先异步地连接到服务器。连接成功后，我们开始异步地读取来自服务器的数据。接收到服务器的数据后，
 * 我们可以在completed方法中处理数据。然后，我们异步地向服务器发送一条消息，并在发送完成后继续异步地读取来自服务器的数据。
 *
 * 请注意，这是一个简单的示例，没有处理异常、关闭资源等细节。在实际应用中，您需要适当地处理这些情况，以确保客户端的稳定性和可靠性。
 *
 * @version 1.0
 * @date 2023-07-27 10:33
 * @since 1.8
 **/
public class AioClientExample {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String serverAddress = "localhost"; // 服务器的IP地址
        int serverPort = 12345; // 服务器的端口号

        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress(serverAddress, serverPort);

        // 异步连接到服务器
        clientChannel.connect(hostAddress, null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.println("连接到服务器成功！");



                // 登录服务器时先打招呼
                String messageToSend = "Hello, Server!";
                ByteBuffer buffer = ByteBuffer.wrap(messageToSend.getBytes());
                clientChannel.write(buffer, null, new CompletionHandler<Integer, Void>() {
                    @Override
                    public void completed(Integer bytesWritten, Void attachment) {
                        // 继续异步读取来自服务器的数据
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        clientChannel.read(readBuffer, null, new CompletionHandler<Integer, Void>() {
                            @Override
                            public void completed(Integer bytesRead, Void attachment) {
                                if (bytesRead > 0) {
                                    readBuffer.flip();
                                    byte[] data = new byte[bytesRead];
                                    readBuffer.get(data);
                                    String serverResponse = new String(data).trim();
                                    System.out.println("收到来自服务器的反馈：" + serverResponse);

                                    // 在此处添加对服务器反馈的处理逻辑

                                    // 继续与服务器沟通，例如发送更多消息
                                }
                            }

                            @Override
                            public void failed(Throwable exc, Void attachment) {
                                System.err.println("读取失败: " + exc.getMessage());
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        System.err.println("写入失败: " + exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println("连接失败: " + exc.getMessage());
            }
        });

        // 让主线程阻塞，保持客户端运行
        Thread.currentThread().join();
    }
}

