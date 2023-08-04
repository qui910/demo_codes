package com.example.netty.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Echo引导服务器
 * <p>
 *   <li>监听和接收进来的连接请求</li>
 *   <li>配置 Channel 来通知一个关于入站消息的 EchoServerHandler 实例</li>
 * </p>
 * @version 1.0
 * @date 2023-08-04 09:33
 * @since 1.8
 **/
@Slf4j
public class EchoServer {
    private final int port;
    public EchoServer(int port) {
        this.port = port;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            log.info("Usage: {} <port>",EchoServer.class.getSimpleName());
            return;
        }
        // 设置端口值（抛出一个 NumberFormatException 如果该端口参数的格式不正确）
        int port = Integer.parseInt(args[0]);
        // 呼叫服务器的 start() 方法
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        // 创建 EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            // 这里只使用一个group
            b.group(group)
                    // 指定 NioServerSocketChannel 为信道类型
                    .channel(NioServerSocketChannel.class)
                    // 设置 socket 地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))
                    // 添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 绑定的服务器;sync 等待服务器关闭
            ChannelFuture f = b.bind().sync();
            log.info("{} started and listen on {}",EchoServer.class.getName(),f.channel().localAddress());
            // 关闭 channel 和 块，直到它被关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
