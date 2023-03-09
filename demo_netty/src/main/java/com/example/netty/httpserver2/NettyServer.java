package com.example.netty.httpserver2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Netty实现上传服务器
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:37
 * @since 1.8
 **/
@Slf4j
public class NettyServer {
    public static final int PORT = 9999;

    public static void run() throws UnknownHostException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(2);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast( new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                            ch.pipeline().addLast(new HttpServerExpectContinueHandler());
                            ch.pipeline().addLast(new FileUploadRequestHandler());
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(PORT).sync();
            log.info("http://{}:{}/ start", InetAddress.getLocalHost().getHostAddress(), PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
