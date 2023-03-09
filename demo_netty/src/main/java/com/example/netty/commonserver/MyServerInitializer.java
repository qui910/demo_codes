package com.example.netty.commonserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                4, 0, 4)) //解码 将二进制数据解码成可读信息
                .addLast(new LengthFieldPrepender(4)) //编码
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new MyServerSocketHandler());
    }
}
