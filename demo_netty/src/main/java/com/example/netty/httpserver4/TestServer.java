package com.example.netty.httpserver4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 使用Netty模拟http服务器
 * 几乎每个Netty实现类都是如此流程的。
 * 结果：浏览器访问http://localhost:8080/ 显示HelloWorld
 */
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        // 事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();// 循环从客户端接收连接，然后转给workGroup
        EventLoopGroup workGroup = new NioEventLoopGroup();// 循环处理从bossGroup转过来的连接

        try {
            // 服务端启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer());  //请求处理器
            // 绑定端口
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
