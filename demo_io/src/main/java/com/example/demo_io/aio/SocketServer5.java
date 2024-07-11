package com.example.demo_io.aio;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Java AIO实现真正异步非阻塞IO 服务端
 * @author pang
 * @version 1.0
 * @date 2024-07-10 15:17
 * @since 1.8
 **/
@Slf4j
public class SocketServer5 {
    private static final Object waitObject = new Object();

    private static final int PORT = 1083;

    private static AsynchronousChannelGroup group;
    private static AsynchronousServerSocketChannel serverChannel;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        /*
         * 对于使用的线程池技术，我一定要多说几句
         * 1、Executors是线程池生成工具，通过这个工具我们可以很轻松的生成“固定大小的线程池”、“调度池”、“可伸缩线程数量的池”。具体请看API Doc
         * 2、当然您也可以通过ThreadPoolExecutor直接生成池。
         * 3、这个线程池是用来得到操作系统的“IO事件通知”的，不是用来进行“得到IO数据后的业务处理的”。要进行后者的操作，您可以再使用一个池（最好不要混用）
         * 4、您也可以不使用线程池（不推荐），如果决定不使用线程池，直接AsynchronousServerSocketChannel.open()就行了。
         * */
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        group = AsynchronousChannelGroup.withThreadPool(threadPool);
        try {
            serverChannel = AsynchronousServerSocketChannel.open(group);
            //设置要监听的端口“0.0.0.0”代表本机所有IP设备
            serverChannel.bind(new InetSocketAddress("0.0.0.0", PORT));
            //为AsynchronousServerSocketChannel注册监听，注意只是为AsynchronousServerSocketChannel通道注册监听
            //并不包括为 随后客户端和服务器 socketchannel通道注册的监听
            serverChannel.accept(null, new AcceptCompletionHandler());

        } catch (Exception e) {
            log.error("Create Aio Error",e);
        }

        //等待，以便观察现象（这个和要讲解的原理本身没有任何关系，只是为了保证守护线程不会退出）
        synchronized(waitObject) {
            waitObject.wait();
        }
    }

/**
 * 这个处理器类，专门用来响应 ServerSocketChannel 的事件。
 * 还记得我们在《架构设计：系统间通信（4）——IO通信模型和JAVA实践 中篇》中所提到的内容吗？ServerSocketChannel只有一种事件：接受客户端的连接
 * @author yinwenjie
 */
@Slf4j
private static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    /**
     * 注意，我们分别观察 this、socketChannel、attachment三个对象的id。
     * 来观察不同客户端连接到达时，这三个对象的变化，以说明ServerSocketChannelHandle的监听模式
     */
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
        log.info("completed(AsynchronousSocketChannel result, ByteBuffer attachment)");
        //为这个新的socketChannel注册“read”事件，以便操作系统在收到数据并准备好后，主动通知应用程序
        //在这里，由于我们要将这个客户端多次传输的数据累加起来一起处理，所以我们将一个stringbuffer对象作为一个“附件”依附在这个channel上
        //
        ByteBuffer readBuffer = ByteBuffer.allocate(50);
        socketChannel.read(readBuffer, readBuffer, new ReadCompletionHandler(socketChannel));

        //每次都要重新注册监听（一次注册，一次响应），但是由于“文件状态标示符”是独享的，所以不需要担心有“漏掉的”事件
        serverChannel.accept(attachment, this);
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
     */
    @Override
    public void failed(Throwable exc, Object attachment) {
        if (exc instanceof AsynchronousCloseException) {
            log.info("Channel was closed while accepting a new connection. Attempting to restart accept operation.");
            // 添加这个代码是因为第一次会启动不成功，需要重新开启接受新连接的操作
            try {
                // 尝试重新开启接受新连接的操作，如果通道仍然开放的话
                serverChannel.accept(null, new AcceptCompletionHandler());
            } catch (Throwable t) {
                log.error("Failed to restart accept operation after channel close exception.", t);
            }
        } else {
            log.error("Error accepting client: " + exc.getMessage(), exc);
        }

        try {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        } catch (IOException e) {
            log.error("Error closing server socket: " + e.getMessage());
        }
    }
}

/**
 * 负责对每一个socketChannel的数据获取事件进行监听。<p>
 *
 * 重要的说明：一个socketchannel都会有一个独立工作的SocketChannelReadHandle对象（CompletionHandler接口的实现），
 * 其中又都将独享一个“文件状态标示”对象FileDescriptor、
 * 一个独立的由程序员定义的Buffer缓存（这里我们使用的是ByteBuffer）、
 * 所以不用担心在服务器端会出现“窜对象”这种情况，因为JAVA AIO框架已经帮您组织好了。<p>
 *
 * 但是最重要的，用于生成channel的对象：AsynchronousChannelProvider是单例模式，无论在哪组socketchannel，
 * 对是一个对象引用（但这没关系，因为您不会直接操作这个AsynchronousChannelProvider对象）。
 * @author yinwenjie
 */
@Slf4j
private static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {


    private AsynchronousSocketChannel socketChannel;


    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //如果条件成立，说明客户端主动终止了TCP套接字，这时服务端终止就可以了
        if(result == -1) {
            try {
                this.socketChannel.close();
            } catch (IOException e) {
                log.error("Read Message Error",e);
            }
            return;
        }

        log.info("completed(Integer result, Void attachment) : 然后我们来取出通道中准备好的值");
        /*
         * 实际上，由于我们从Integer result知道了本次channel从操作系统获取数据总长度
         * 所以实际上，我们不需要切换成“读模式”的，但是为了保证编码的规范性，还是建议进行切换。
         *
         * 另外，无论是JAVA AIO框架还是JAVA NIO框架，都会出现“buffer的总容量”小于“当前从操作系统获取到的总数据量”，
         * 但区别是，JAVA AIO框架中，我们不需要专门考虑处理这样的情况，因为JAVA AIO框架已经帮我们做了处理（做成了多次通知）
         * */
        buffer.flip();
        byte[] contexts = new byte[1024];
        buffer.get(contexts, 0, result);
        String nowContent = new String(contexts , 0 , result , StandardCharsets.UTF_8);
        log.info("================目前的传输结果：" + nowContent);


        //如果条件成立，说明还没有接收到“结束标记”
        if(!nowContent.contains("over")) {
            this.socketChannel.read(buffer, buffer, this);
            return;
        }

        //=========================================================================
        //          和上篇文章的代码相同，我们以“over”符号作为客户端完整信息的标记
        //=========================================================================
        log.info("=======收到完整信息，开始处理业务=========");
        // Write response back to the client
        ByteBuffer outBuffer = ByteBuffer.wrap("回发响应信息！".getBytes(StandardCharsets.UTF_8));
//        socketChannel.write(outBuffer, outBuffer, new WriteCompletionHandler(socketChannel));  //
//        此种方式无法反馈消息到client，问题待查

        Future<Integer> writeResult = socketChannel.write(outBuffer); // 此种方式反馈了消息，但是client还是无法从  in.readLine();获取到，待查？？
        log.info("Write Answer is [{}]", JSONUtil.toJsonStr(writeResult));

        //还要继续监听（一次监听一次通知）
        this.socketChannel.read(buffer, buffer, this);
    }

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
     */
    @Override
    public void failed(Throwable exc, ByteBuffer historyContext) {
        if (exc instanceof AsynchronousCloseException) {
            log.error("Channel was closed while reading from a client.");
        } else {
            log.error("Error reading from client: " + exc.getMessage(), exc);
        }

        try {
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            log.error("Error closing client socket: " + e.getMessage());
        }
    }
}

    @Slf4j
    private static class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private AsynchronousSocketChannel socketChannel;


        public WriteCompletionHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }
        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            if (result>0) {
                // Retry write operation
                socketChannel.write(buffer, buffer, this);
                log.info("Writer Answer");
            } else {
                // Write operation complete
                buffer.clear();
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            log.error("Error writing to client: " + exc.getMessage(), exc);
        }
    }

}
