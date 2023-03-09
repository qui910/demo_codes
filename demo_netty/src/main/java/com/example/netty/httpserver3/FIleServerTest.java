package com.example.netty.httpserver3;

import org.junit.Test;

import java.net.UnknownHostException;

/**
 * Netty实现多文件上传下载测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:52
 * @since 1.8
 **/
public class FIleServerTest {

    @Test
    public void testStartServer() throws UnknownHostException, InterruptedException {
        FileServer.run();
    }
}
