package com.example.netty.httpserver4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 自定义Http处理器
 * 使用Netty开发Http，比servlet更为底层，性能会更为好一些，但是比servlet和tomcat的差别就是太底层了
 * 连请求路由都不支持。
 * 由浏览器测试 连接地址http://localhost:8080
 * 运行结果：
 * handler Added
 * channel Registered
 * handler Added
 * channel Active
 * channel Registered
 * channel Active
 * 请求方法名：GET
 * 请求URI：/
 * 请求方法名：GET
 * 请求URI：/favicon.ico
 * 请求favicon.ico
 * ---------------------
 * 请求方法名：GET
 * 请求URI：/
 * 请求方法名：GET
 * 请求URI：/favicon.ico
 * 请求favicon.ico
 * ---------------------
 * channel Inactive
 * channel Unregistered
 * handler Added
 * channel Registered
 * channel Active
 * 请求方法名：GET
 * 请求URI：/
 * 请求方法名：GET
 * 请求URI：/favicon.ico
 * 请求favicon.ico
 * 分析结果：netty并不遵循servlet规范
 * 浏览器在一起请求后，并不关闭连接所以 Inactive，Unregistered是在浏览器关闭时才触发的。
 * 底层还是serversocket连接，如果是http1.0是短连接，是请求完成后就关闭连接。如果是http1.1则有keepalive时间，只有
 * 时间到了之后才会关闭。
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 新通道被添加时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler Added");
        super.handlerAdded(ctx);
    }

    /**
     * 通道被注册时添加
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Registered");
        super.channelRegistered(ctx);
    }

    /**
     * 通道变为活动状态时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Active");
        super.channelActive(ctx);
    }


    /**
     * 接收到消息后触发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // class io.netty.handler.codec.http.DefaultHttpRequest
        // class io.netty.handler.codec.http.LastHttpContent$1
        System.out.println(msg.getClass());

        // /0:0:0:0:0:0:0:1:13058
        System.out.println(ctx.channel().remoteAddress());

        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;

            System.out.println("请求方法名：" + httpRequest.method().name());
            System.out.println("请求URI：" + httpRequest.uri().toString());
            URI uri = new URI(httpRequest.uri());
            // 浏览器在正常请求的基本上，还会请求/favicon.ico 即网站图标
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
            }

            ByteBuf context = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.ACCEPTED.OK, context);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                    .set(HttpHeaderNames.CONTENT_LENGTH, context.readableBytes());

            ctx.writeAndFlush(response);

            // close主动关闭连接，不判断是否是HTTP1.0还是1.1
//            ctx.channel().close();
        }
    }

    /**
     * 通道变为不活动时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Inactive");
        super.channelInactive(ctx);
    }

    /**
     * 通道注销时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel Unregistered");
        super.channelUnregistered(ctx);
    }
}
