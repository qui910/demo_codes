package com.example.netty.commonserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoop = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoop)
                    .channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());//客户端使用handler

            ChannelFuture channelFuture = bootstrap.connect("localhost",
                    8081).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoop.shutdownGracefully();
        }
    }
}
