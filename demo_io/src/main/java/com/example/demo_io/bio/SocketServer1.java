package com.example.demo_io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * java bio 阻塞模式的支持
 * @author pang
 * @version 1.0
 * @date 2024-07-06 11:16
 * @since 1.8
 **/
@Slf4j
public class SocketServer1 {

    public static void main(String[] args){
        try (ServerSocket serverSocket = new ServerSocket(1083)) {
            log.info("server on port [{}] is running", 1083);
            while(true) {
                //这里JAVA通过JNI请求操作系统，并一直等待操作系统返回结果（或者出错）
                Socket socket = serverSocket.accept();

                //同步阻塞处理客户端连接
                clientHandler(socket);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 同步阻塞接收处理消息
     * @param clientSocket socket连接
     */
    private static void clientHandler(Socket clientSocket) {
        //下面我们收取信息（这里还是阻塞式的,一直等待，直到有数据可以接受）
        try(InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream()) {
            int sourcePort = clientSocket.getPort();
            int maxLen = 2048;
            byte[] contextBytes = new byte[maxLen];
            int realLen;
            StringBuilder message = new StringBuilder();
            //read的时候，程序也会被阻塞，直到操作系统把网络传来的数据准备好。
            while((realLen = in.read(contextBytes, 0, maxLen)) != -1) {
                message.append(new String(contextBytes , 0 , realLen));
                /*
                 * 我们假设读取到“over”关键字，
                 * 表示客户端的所有信息在经过若干次传送后，完成
                 * */
                if(message.indexOf("over") != -1) {
                    break;
                }
            }
            //下面打印信息
            log.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

            //下面开始发送信息
            out.write("回发响应信息！".getBytes());
        } catch (IOException e) {
            log.error("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.error("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
