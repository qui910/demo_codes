package com.example.netty.httpserver1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 非上传下载的路径时，其他路径均非法
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:17
 * @since 1.8
 **/
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        GeneralResponse generalResponse;
        //错误处理
        generalResponse = new GeneralResponse(HttpResponseStatus.BAD_REQUEST, "请检查你的请求方法及url", null);
        ResponseUtil.response(ctx, request, generalResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        log.warn("错误{}", e.getMessage());
        ctx.close();
    }
}
