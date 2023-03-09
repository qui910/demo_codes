package com.example.netty.httpserver;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Netty实现简单文件服务器测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 09:58
 * @since 1.8
 **/
@Slf4j
public class FileServerTest {

    @Test
    public void testStartHttpFileServer() throws Exception {
        HttpFileServer fileServer = new HttpFileServer();
        fileServer.run(9090,"/src/");
    }
}
