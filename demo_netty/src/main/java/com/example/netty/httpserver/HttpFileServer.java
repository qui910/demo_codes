package com.example.netty.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过Netty模拟一个Http的文件服务器
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 09:54
 * @since 1.8
 **/
@Slf4j
public class HttpFileServer {
    private static final String DEFAULT_URL = "/src/";

    public void run(final int port, final String url)throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture f = b.bind("localhost", port).sync();
            log.info("HTTP 文件服务器启动, 地址是： " + "http://localhost:" + port + url);
            f.channel().closeFuture().sync();

        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
