package com.example.netty.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 一个服务器handler - ChannelHandler：这个组件实现了服务器的业务逻辑，决定了连接创建后和接收到信息后该如何处理
 * <p>
 * Echo Server 将会将接受到的数据的拷贝发送给客户端。因此，我们需要实现 ChannelInboundHandler 接口，用来定义处理入站事件的方法。
 * 由于我们的应用很简单，只需要继承 ChannelInboundHandlerAdapter 就行了。这个类提供了默认 ChannelInboundHandler 的实现，
 * 所以只需要覆盖下面的方法：
 *
 * <li>channelRead() - 每个信息入站都会调用</li>
 * <li>channelReadComplete() - 通知处理器最后的 channelRead() 是当前批处理中的最后一条消息时调用</li>
 * <li>exceptionCaught()- 读操作时捕获到异常时调用</li>
 * </p>
 * @version 1.0
 * @date 2023-08-04 09:12
 * @since 1.8
 **/
@ChannelHandler.Sharable  // @Sharable标识这类的实例之间可以在 channel 里面共享
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        // 日志消息输出到控制台
        log.info("Server received: {}",in.toString(CharsetUtil.UTF_8));
        // 将所接收的消息返回给发送者。注意，这还没有冲刷数据
        ctx.write(in);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 冲刷所有待审消息到远程节点。关闭通道后，操作完成
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 每个Channel都有一个关联的ChannelPipeline，它代表了ChannelHandler实例的链。适配器处理的实现只是将一个处理方法调用转发到链中的
     * 下一个处理器。因此，如果一个Netty应用程序不覆盖exceptionCaught ，那么这些错误将最终到达ChannelPipeline，并且结束警告将被记录。
     * 出于这个原因，你应该提供至少一个实现exceptionCaught的ChannelHandler。
     *
     * 覆盖exceptionCaught使我们能够应对任何Throwable的子类型。在这种情况下我们记录，并关闭所有可能处于未知状态的连接。它通常是难以
     * 从连接错误中恢复，所以干脆关闭远程连接。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        // 打印异常堆栈跟踪
        cause.printStackTrace();
        // 关闭通道
        ctx.close();
    }
}
