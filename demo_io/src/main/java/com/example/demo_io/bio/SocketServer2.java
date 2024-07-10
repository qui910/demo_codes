package com.example.demo_io.bio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * java bio 超时模式（非阻塞） + 多线程
 * <p>
 *     较SocketServer1相比，有3个变更点：
 *     1. accept设置超时时间
 *     2. read设置超时时间
 *     3，使用线程池
 * </p>
 * @author pang
 * @version 1.0
 * @date 2024-07-09 13:03
 * @since 1.8
 **/
@Slf4j
public class SocketServer2 {

    private static final Object xWait = new Object();
    public static void main(String[] args){
        try (ServerSocket serverSocket = new ServerSocket(1083)) {
            serverSocket.setSoTimeout(1000);
            log.info("server on port [{}] is running", 1083);
            while(true) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("Step1 socket accept begin");
                Socket socket;
                try {
                    socket = serverSocket.accept();
                    stopWatch.stop();
                    log.info(stopWatch.prettyPrint());
                } catch(SocketTimeoutException e1) {
                    stopWatch.stop();
                    stopWatch.start("Step2 socket accept timeout");
                    //===========================================================
                    //      变更点1，执行到这里，说明本次accept没有接收到任何数据报文
                    //      主线程在这里就可以做一些事情，记为X
                    //===========================================================
                    synchronized (SocketServer2.xWait) {
                        log.info("这次没有从底层接收到任务数据报文，等待5秒，模拟事件X的处理时间");
                        SocketServer2.xWait.wait(5000);
                    }
                    stopWatch.stop();
                    // 根据stopWatch记录结果得知，serverSocket.accept(); 大概1s
                    // xWait.wait 大概5s
                    log.info(stopWatch.prettyPrint());
                    continue;
                }

                // 变更点 2
                // 当然业务处理过程可以交给一个线程（这里可以使用线程池）,并且线程的创建是很耗资源的。
                // 最终改变不了.accept()只能一个一个接受socket连接的情况
                SocketServerThread socketServerThread = new SocketServerThread(socket);
                new Thread(socketServerThread).start();
            }
        } catch (InterruptedException e) {
            // 重新设置中断状态
            Thread.currentThread().interrupt();
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 当然，接收到客户端的socket后，业务的处理过程可以交给一个线程来做。
     * 但还是改变不了socket被一个一个的做accept()的情况。
     */
   static class SocketServerThread implements Runnable {

        private Socket socket;

        public SocketServerThread (Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            clientHandler(socket);
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
            //下面我们收取信息（设置成非阻塞方式，这样read信息的时候，又可以做一些其他事情）
            clientSocket.setSoTimeout(1000);
            BIOREAD:while (true) {
                try {
                    while((realLen = in.read(contextBytes, 0, maxLen)) != -1) {
                        message.append(new String(contextBytes , 0 , realLen));
                        /*
                         * 我们假设读取到“over”关键字，
                         * 表示客户端的所有信息在经过若干次传送后，完成
                         * */
                        if(message.indexOf("over") != -1) {
                            break BIOREAD;
                        }
                    }
                } catch (SocketTimeoutException e) {
                    //===========================================================
                    //      变更单3，执行到这里，说明本次read没有接收到任何数据流
                    //      主线程在这里又可以做一些事情，记为Y
                    //===========================================================
                    synchronized (SocketServer2.xWait) {
                        log.info("这次没有从底层接收到任务数据报文，等待5秒，模拟事件Y的处理时间");
                        SocketServer2.xWait.wait(5000);
                    }
                }
            }
            //下面打印信息
            log.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

            //下面开始发送信息
            out.write("回发响应信息！".getBytes());
        } catch (InterruptedException e) {
            // 重新设置中断状态
            Thread.currentThread().interrupt();
        }  catch (IOException e) {
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
