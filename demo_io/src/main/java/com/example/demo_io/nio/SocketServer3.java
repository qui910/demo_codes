package com.example.demo_io.nio;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 使用Java NIO实现Socket通信
 * @author pang
 * @version 1.0
 * @date 2024-07-10 11:13
 * @since 1.8
 **/
@Slf4j
public class SocketServer3 {

    private static final int PORT = 1083;

    private static final Object xWait = new Object();
    public static void main(String[] args) throws Exception {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open();){
            serverChannel.socket().bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false); // 设置为非阻塞模式，这是必须的
            //注意、服务器通道只能注册SelectionKey.OP_ACCEPT事件
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            log.info("NIO server on port [{}] is running", PORT);

            while(true) {
                //如果条件成立，说明本次询问selector，并没有获取到任何准备好的、感兴趣的事件
                //java程序对多路复用IO的支持也包括了阻塞模式 和非阻塞模式两种。但是一般都默认非阻塞模式

                // 设置5s超时， 无论阻塞还是非阻塞模式，这里都会会阻塞，直到有事件发生，
                if(selector.select(1000) == 0) {
                    //================================================
                    //      这里视业务情况，可以做一些然并卵的事情
                    //================================================
                    synchronized (SocketServer3.xWait) {
                        log.info("这次没有从底层接收到连接请求，等待5秒，模拟事件X的处理时间");
                        SocketServer3.xWait.wait(5000);
                    }
                    continue;
                }
                //这里就是本次询问操作系统，所获取到的“所关心的事件”的事件类型（每一个通道都是独立的）
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

                while(selectionKeys.hasNext()) {
                    SelectionKey readyKey = selectionKeys.next();
                    //这个已经处理的readyKey一定要移除。如果不移除，就会一直存在在selector.selectedKeys集合中
                    //待到下一次selector.select() > 0时，这个readyKey又会被处理一次
                    selectionKeys.remove();

                    if(readyKey.isValid() && readyKey.isAcceptable()) {
                        log.info("======channel通道已经准备好=======");
                        /*
                         * 当server socket channel通道已经准备好，就可以从server socket channel中获取socketchannel了
                         * 拿到socket channel后，要做的事情就是马上到selector注册这个socket channel感兴趣的事情。
                         * 否则无法监听到这个socket channel到达的数据
                         * */
                        registerSocketChannel(serverChannel , selector);

                    } else if(readyKey.isValid() && readyKey.isConnectable()) {
                        log.info("======socket channel 建立连接=======");
                    } else if(readyKey.isValid() && readyKey.isReadable()) {
                        log.info("======socket channel 数据准备完成，可以去读==读取=======");
                        readSocketChannel(readyKey);
                    }
                }
            }
        } catch(Exception e) {
            log.error("Error setting up the NIO server: " + e.getMessage(), e);
        }
    }

    /**
     * 在server socket channel接收到/准备好 一个新的 TCP连接后。
     * 就会向程序返回一个新的socketChannel。<br>
     * 但是这个新的socket channel并没有在selector“选择器/代理器”中注册，
     * 所以程序还没法通过selector通知这个socket channel的事件。
     * 于是我们拿到新的socket channel后，要做的第一个事情就是到selector“选择器/代理器”中注册这个
     * socket channel感兴趣的事件
     * @param socketChannel 新的socket channel
     * @param selector selector“选择器/代理器”
     * @throws Exception
     */
    private static void registerSocketChannel(ServerSocketChannel socketChannel , Selector selector) throws Exception {
        SocketChannel clientChannel = socketChannel.accept();
        clientChannel.configureBlocking(false); // 设置为非阻塞模式
        //socket通道可以且只可以注册三种事件SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT
        clientChannel.register(selector, SelectionKey.OP_READ);
        log.info("Accepted new client connection.");
    }

    /**
     * 这个方法用于读取从客户端传来的信息。
     * 并且观察从客户端过来的socket channel在经过多次传输后，是否完成传输。
     * 如果传输完成，则返回一个true的标记。
     * @throws Exception
     */
    private static void readSocketChannel(SelectionKey readyKey) throws Exception {
        SocketChannel clientSocketChannel = (SocketChannel)readyKey.channel();
        //获取客户端使用的端口
        InetSocketAddress sourceSocketAddress = (InetSocketAddress)clientSocketChannel.getRemoteAddress();
        Integer resoucePort = sourceSocketAddress.getPort();

        //拿到这个socket channel使用的缓存区，准备读取数据
        //在后文，将详细讲解缓存区的用法概念，实际上重要的就是三个元素capacity,position和limit。
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        //将通道的数据写入到缓存区，注意是写入到缓存区。
        //由于之前设置了ByteBuffer的大小为2048 byte，所以可以存在写入不完的情况
        //没关系，我们后面来调整代码。这里我们暂时理解为一次接受可以完成
        int realLen = -1;
        try {
            realLen = clientSocketChannel.read(buffer);
        } catch(Exception e) {
            //这里抛出了异常，一般就是客户端因为某种原因终止了。所以关闭channel就行了
            log.error(e.getMessage());
            clientSocketChannel.close();
            return;
        }

        //如果缓存区中没有任何数据（但实际上这个不太可能，否则就不会触发OP_READ事件了）
        if(realLen == -1) {
            log.warn("====缓存区没有数据？====");
            return;
        }

        //将缓存区从写状态切换为读状态（实际上这个方法是读写模式互切换）。
        //这是java nio框架中的这个socket channel的写请求将全部等待。
        buffer.flip();
        //注意中文乱码的问题
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        String message = new String(data, StandardCharsets.UTF_8);

        //如果收到了“over”关键字，才会清空buffer，并回发数据；
        //否则不清空缓存，还要还原buffer的“写状态”
        if(message.contains("over")) {
            //清空已经读取的缓存，并从新切换为写状态(这里要注意clear()和capacity()两个方法的区别)
            buffer.clear();
            log.info("端口:" + resoucePort + "客户端发来的信息======message : " + message);

            //======================================================
            //          当然接受完成后，可以在这里正式处理业务了
            //======================================================

            //回发数据，并关闭channel
            ByteBuffer sendBuffer = ByteBuffer.wrap("回发响应信息！".getBytes(StandardCharsets.UTF_8));
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            log.info("端口:" + resoucePort + "客户端信息还未接受完，继续接受======message : " + message);
            //这是，limit和capacity的值一致，position的位置是realLen的位置
            buffer.position(realLen);
            buffer.limit(buffer.capacity());
        }
    }
}
