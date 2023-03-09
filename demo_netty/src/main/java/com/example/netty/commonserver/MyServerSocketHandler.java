package com.example.netty.commonserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * 通用socket与客户端传递先定义传输string，所以用String泛型
 */
public class MyServerSocketHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
        System.out.println("消息信息为:" + msg);
        //消息多时，调用多个write，在调用flash
        ctx.channel().writeAndFlush("from server：" + UUID.randomUUID());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
