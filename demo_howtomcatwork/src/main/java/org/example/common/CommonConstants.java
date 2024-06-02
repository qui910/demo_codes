package org.example.common;

import java.io.File;

/**
 * 通用常量
 */
public class CommonConstants {

    /**
     * 默认IP
     */
    public static final String IP_ADDRESS = "127.0.0.1";

    /**
     * 默认端口
     */
    public static final int PORT = 8080;

    /**
     * web根目录
     */
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "demo_howtomcatwork" + File.separator  + "webroot";
    /**
     * 关闭命令
     */
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    /**
     * 缓冲区大小
     */
    public static final int BUFFER_SIZE = 2048;

    /**
     * HTTP响应头
     */
    public static final String HTTP_RESPONSE_HEADER =
            "HTTP/1.1 200 OK\r\n" +
                    "Service: Tomcat 1.0\r\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                    "Content-Type: text/html\r\n";


    /**
     * HTTP响应头
     */
    public static final String HTTP_RESPONSE_IMAGE_GIF_HEADER =
            "HTTP/1.1 200 OK\r\n" +
                    "Service: Tomcat 1.0\r\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                    "Content-Type: image/gif\r\n";

    /**
     * HTTP响应头
     */
    public static final String HTTP_RESPONSE_IMAGE_ICON_HEADER =
            "HTTP/1.1 200 OK\r\n" +
                    "Service: Tomcat 1.0\r\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                    "Content-Type: image/icon\r\n";

    /**
     * 404响应头
     */
    public static final String HTTP_RESPONSE_HEADER_404 =
            "HTTP/1.1 404 Not Found\r\n" +
                    "Service: Tomcat 1.0\r\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
}
