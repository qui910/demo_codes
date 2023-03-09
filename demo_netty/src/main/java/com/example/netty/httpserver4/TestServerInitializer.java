package com.example.netty.httpserver4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 请求处理器初始化
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 这里必须每次new个对象
        pipeline.addLast("HttpServerCodec", new HttpServerCodec()) //编解码
                .addLast("TestHttpServerHandler", new TestHttpServerHandler());
    }
}
