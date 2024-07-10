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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用Java NIO实现Socket通信
 * <p>
 *     缓存使用改进
 * </p>
 * @author pang
 * @version 1.0
 * @date 2024-07-10 11:13
 * @since 1.8
 **/
@Slf4j
public class SocketServer4 {

    private static final int PORT = 1083;

    private static final Object xWait = new Object();

    /**
     * 改进点1
     * 改进的java nio server的代码中，由于buffer的大小设置的比较小。
     * 我们不再把一个client通过socket channel多次传给服务器的信息保存在beff中了（因为根本存不下）<br>
     * 我们使用socketchanel的hashcode作为key（当然您也可以自己确定一个id），信息的stringbuffer作为value，存储到服务器端的一个内存区域MESSAGEHASHCONTEXT。
     *
     * 如果您不清楚ConcurrentHashMap的作用和工作原理，请自行百度/Google
     */
    private static final ConcurrentMap<Integer, StringBuilder> MESSAGEHASHCONTEXT = new ConcurrentHashMap<Integer , StringBuilder>();

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
                    synchronized (SocketServer4.xWait) {
                        log.info("这次没有从底层接收到连接请求，等待5秒，模拟事件X的处理时间");
                        SocketServer4.xWait.wait(5000);
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
        SocketChannel clientSocketChannel = (SocketChannel) readyKey.channel();
        //获取客户端使用的端口
        InetSocketAddress sourceSocketAddress = (InetSocketAddress) clientSocketChannel.getRemoteAddress();
        Integer resoucePort = sourceSocketAddress.getPort();

        //拿到这个socket channel使用的缓存区，准备读取数据
        //在后文，将详细讲解缓存区的用法概念，实际上重要的就是三个元素capacity,position和limit。
        ByteBuffer buffer = ByteBuffer.allocate(50);
        //将通道的数据写入到缓存区，注意是写入到缓存区。
        //这次，为了演示buff的使用方式，我们故意缩小了buff的容量大小到50byte，
        //以便演示channel对buff的多次读写操作
        int realLen = 0;
        StringBuilder message = new StringBuilder();
        //这句话的意思是，将目前通道中的数据写入到缓存区
        //最大可写入的数据量就是buff的容量
        while ((realLen = clientSocketChannel.read(buffer)) != 0) {

            //一定要把buffer切换成“读”模式，否则由于limit = capacity
            //在read没有写满的情况下，就会导致多读
            buffer.flip();
            int position = buffer.position();
            int capacity = buffer.capacity();
            byte[] messageBytes = new byte[capacity];
            buffer.get(messageBytes, position, realLen);

            //这种方式也是可以读取数据的，而且不用关心position的位置。
            //因为是目前contextBytes所有的数据全部转出为一个byte数组。
            //使用这种方式时，一定要自己控制好读取的最终位置（realLen很重要）
            //byte[] messageBytes = contextBytes.array();

            //注意中文乱码的问题
            String messageEncode = new String(messageBytes, 0, realLen, StandardCharsets.UTF_8);
            message.append(messageEncode);

            //再切换成“写”模式，直接情况缓存的方式，最快捷
            buffer.clear();
        }

        //如果发现本次接收的信息中有over关键字，说明信息接收完了
        if (message.toString().contains("over")) {
            //则从messageHashContext中，取出之前已经收到的信息，组合成完整的信息
            Integer channelUUID = clientSocketChannel.hashCode();
            log.info("端口:" + resoucePort + "客户端发来的信息======message : " + message);
            StringBuilder completeMessage;
            //清空MESSAGEHASHCONTEXT中的历史记录
            StringBuilder historyMessage = MESSAGEHASHCONTEXT.remove(channelUUID);
            if (historyMessage == null) {
                completeMessage = message;
            } else {
                completeMessage = historyMessage.append(message);
            }
            log.info("端口:" + resoucePort + "客户端发来的完整信息======completeMessage : " + completeMessage.toString());

            //======================================================
            //          当然接受完成后，可以在这里正式处理业务了
            //======================================================

            //回发数据，并关闭channel
            ByteBuffer sendBuffer = ByteBuffer.wrap("回发响应信息！".getBytes(StandardCharsets.UTF_8));
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            //如果没有发现有“over”关键字，说明还没有接受完，则将本次接受到的信息存入messageHashContext
            log.info("端口:" + resoucePort + "客户端信息还未接受完，继续接受======message : " + message.toString());
            //每一个channel对象都是独立的，所以可以使用对象的hash值，作为唯一标示
            Integer channelUUID = clientSocketChannel.hashCode();

            //然后获取这个channel下以前已经达到的message信息
            StringBuilder historyMessage = MESSAGEHASHCONTEXT.get(channelUUID);
            if (historyMessage == null) {
                historyMessage = new StringBuilder();
                MESSAGEHASHCONTEXT.put(channelUUID, historyMessage.append(message));
            }
        }
    }

}
