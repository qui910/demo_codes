package com.example.netty.httpserver1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Netty实现上传下载的Ftp服务器测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:18
 * @since 1.8
 **/
@Slf4j
public class FtpServerTest {

    @Test
    public void testStartFtpServer() throws Exception {
        NettyServer.run();
    }
}
