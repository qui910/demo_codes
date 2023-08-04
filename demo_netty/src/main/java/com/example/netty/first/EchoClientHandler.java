package com.example.netty.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 跟写服务器一样，我们提供 ChannelInboundHandler 来处理数据。
 * @version 1.0
 * @date 2023-08-04 13:15
 * @since 1.8
 **/
@Slf4j
@ChannelHandler.Sharable  // 标记这个类的实例可以在 channel 里共享
public class EchoClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 建立连接后该 channelActive() 方法被调用一次。
     * 逻辑很简单：一旦建立了连接，字节序列被发送到服务器。该消息的内容并不重要;在这里，我们使用了 Netty 编码字符串 “Netty rocks!”
     * 通过覆盖这种方法，我们确保东西被尽快写入到服务器。
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 当被通知该 channel 是活动的时候就发送信息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
    }

    /**
     * 在接收到数据时被调用.
     * 当服务器发送 5 个字节是不是保证所有的 5 个字节会立刻收到 - 即使是只有 5 个字节，channelRead0() 方法可被调用两次，
     * 第一次用一个ByteBuf（Netty的字节容器）装载3个字节和第二次一个 ByteBuf 装载 2 个字节。
     * 唯一要保证的是，该字节将按照它们发送的顺序分别被接收。（注意，这是真实的，只有面向流的协议如TCP）
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        // 记录接收到的消息
        log.info("Client received: {}",in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        // 记录日志错误并关闭 channel
        cause.printStackTrace();
        ctx.close();
    }
}
