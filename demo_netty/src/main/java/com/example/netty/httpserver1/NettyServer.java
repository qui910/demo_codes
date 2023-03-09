package com.example.netty.httpserver1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * Netty实现上传下载的Ftp服务器
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:09
 * @since 1.8
 **/
@Slf4j
public class NettyServer {
    public static final int PORT = 9999;

    public static void run() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(boss)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //传递路由类
                .childHandler(new NettyServerInitializer());

        Channel ch = b.bind(PORT).sync().channel();
        log.info("http://{}:{}/ start, only supper url that contains download or upload",
                InetAddress.getLocalHost().getHostAddress(), PORT);
        ch.closeFuture().sync();
    }
}
