package com.example.netty.chatqq;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 聊天室Server
 * 1：N 的方式
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:53
 * @since 1.8
 **/
@Slf4j
public class MyChatServer {
    public static final int PORT = 8081;

    public static void run() throws InterruptedException, UnknownHostException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyChatServerInitializer());

            ChannelFuture channelFuture = bootstrap.bind(8081).sync();
            log.info("聊天室服务器地址：http://{}:{}/ start", InetAddress.getLocalHost().getHostAddress(), PORT);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
