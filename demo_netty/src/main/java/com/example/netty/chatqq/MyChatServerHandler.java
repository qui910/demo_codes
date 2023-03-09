package com.example.netty.chatqq;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:57
 * @since 1.8
 **/
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 保存所有已经连接的channel对象
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 向其他客户端发送消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channels.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("【" + channel.remoteAddress() + "】发送的消息：" + msg);
            } else {
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 连接新建
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 向所有channel发送消息
        channels.writeAndFlush("【客户端】-" + channel.remoteAddress() + " 加入\n");
        channels.add(channel);
    }

    /**
     * 连接断掉
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("【客户端】-" + channel.remoteAddress() + " 离开\n");
        // channels.remove(channel); //连接断掉服务器会自动执行，这里可以不用写
    }

    //连接活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("【客户端】-" + channel.remoteAddress() + " 上线\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("【客户端】-" + channel.remoteAddress() + " 下线\n");
    }
}
