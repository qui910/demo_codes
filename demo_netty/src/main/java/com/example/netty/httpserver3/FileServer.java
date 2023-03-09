package com.example.netty.httpserver3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Netty实现多文件上传下载
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:44
 * @since 1.8
 **/
@Slf4j
public class FileServer {
    private final int port;
    public FileServer(int port) {
        this.port = port;
    }

    public static void run() throws InterruptedException, UnknownHostException {
        int port = 9999;
        FileServer fileServer = new FileServer(port);
        log.info("服务器即将启动,http://{}:{}/",InetAddress.getLocalHost().getHostAddress(), port);
        fileServer.start();
    }

    public void start() throws InterruptedException {
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        Pipeline pipeline = new Pipeline();
        try {
            /*服务端启动必须*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)/*将线程组传入*/
                    .channel(NioServerSocketChannel.class)/*指定使用NIO进行网络传输*/
                    .localAddress(new InetSocketAddress(port))/*指定服务器监听端口*/
                    /*服务端每接收到一个连接请求，就会新启一个socket通信，也就是channel，
                    所以下面这段代码的作用就是为这个子channel增加handle*/
                    .childHandler(pipeline);
            ChannelFuture f = b.bind().sync();/*异步绑定到服务器，sync()会阻塞直到完成*/
            f.channel().closeFuture().sync();/*阻塞直到服务器的channel关闭*/

        } finally {
            group.shutdownGracefully().sync();/*优雅关闭线程组*/
        }

    }
}
