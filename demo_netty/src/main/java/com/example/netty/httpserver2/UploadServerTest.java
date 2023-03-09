package com.example.netty.httpserver2;

import org.junit.Test;

import java.net.UnknownHostException;

/**
 * Netty实现上传服务器测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:42
 * @since 1.8
 **/
public class UploadServerTest {

    @Test
    public void testStartServer() throws UnknownHostException {
        NettyServer.run();
    }
}
