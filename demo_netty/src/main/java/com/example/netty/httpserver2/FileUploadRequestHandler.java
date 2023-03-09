package com.example.netty.httpserver2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.FileOutputStream;
import java.util.List;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:38
 * @since 1.8
 **/
public class FileUploadRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        HttpDataFactory factory = new DefaultHttpDataFactory(true);
        HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
        httpDecoder.setDiscardThreshold(0);
        final HttpContent chunk = fullHttpRequest;
//        httpDecoder.offer(chunk);
        if (chunk instanceof LastHttpContent) {
            List<InterfaceHttpData> interfaceHttpDataList = httpDecoder.getBodyHttpDatas();
            for (InterfaceHttpData data : interfaceHttpDataList) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUpload fileUpload = (FileUpload) data;
                    try( FileOutputStream fileOutputStream =
                                 new FileOutputStream("/home/pang/upload/"+((FileUpload) data).getFilename()) ) {
                        fileOutputStream.write(fileUpload.get());
                        fileOutputStream.flush();
                    }
                }
                //如果数据类型为参数类型，则保存到body对象中
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute){
                    Attribute attribute = (Attribute) data;
                    System.out.println(attribute.getName() + ":" + attribute.getValue());
                }
            }
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
                io.netty.handler.codec.http.HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer("hello world"+String.valueOf(System.currentTimeMillis()), CharsetUtil.UTF_8));
        response.headers().set("Content-Type", "text/plain");
        response.headers().set("Content-Length", response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        channelHandlerContext.writeAndFlush(response);
    }
}
