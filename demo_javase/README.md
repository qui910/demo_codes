[toc]
>>本Model主要展示一些JavaSE Demo

# 1 IO

## 1.1 AIO
AIO是Java 7中引入的更高级别的异步I/O模型。它提供了更加简洁的API来处理异步I/O操作。在AIO中，当一个I/O操作完成时，会触发一个通知，而不需要显式地进行轮询或阻塞等待。这种模型适用于高并发且处理时间较长的I/O操作，例如网络通信或文件处理。

AIO适用于处理连接数较多且处理时间较长的I/O操作，它的性能较NIO略有提升，并且编程模型更加简洁。
## 1.2 BIO
BIO是Java最传统的I/O模型，也称为阻塞式I/O。在BIO中，每个I/O操作（读或写）都会阻塞当前线程，直到数据就绪或操作完成。这意味着一个线程一次只能处理一个I/O请求，而其他请求必须等待。如果并发请求很多，就需要创建大量线程，这可能导致系统资源浪费和性能下降。

BIO适用于连接数较小且并发较低的情况，易于理解和使用，但在高并发场景下性能较差。

## 1.3 NIO
NIO是Java提供的新I/O模型，也称为非阻塞式I/O。NIO引入了Channel和Buffer的概念，使得可以使用单个线程处理多个I/O请求。在NIO中，当一个操作不可立即完成时，线程不会被阻塞，而是可以继续处理其他任务。这样可以更高效地利用线程和系统资源，并提高系统的吞吐量。

NIO适用于处理连接数较多且并发较高的情况，可以更好地利用系统资源，但编程模型较为复杂。

## 1.4 参考资料
- [【深入理解Java】一篇文章带你彻底吃透Java NIO](https://juejin.cn/post/7195989541190221861)
- [网络编程三剑客BIO/NIO/AIO](https://dev-james.xyz/archives/%E7%BD%91%E7%BB%9C%E7%BC%96%E7%A8%8B%E4%B8%89%E5%89%91%E5%AE%A2bionioaio)
- [JAVA NIO深入剖析](http://itsoku.com/course/23/396)


# 2 NIO主要结构

## 2.1 SelectableChannel
`SelectableChannel`是Java NIO（New I/O）中的一个关键抽象类，它表示一个可选择的通道。可选择的通道是一种可以注册到选择器（`Selector`）上，以便在通道上进行I/O操作时进行事件驱动处理的通道。

`SelectableChannel`是所有NIO通道的父类，它包括了以下主要子类：

1. `SocketChannel`：用于TCP网络通信的通道，可以进行连接、读取和写入操作。

2. `ServerSocketChannel`：用于TCP网络通信的服务器端通道，用于接受客户端连接请求。

3. `DatagramChannel`：用于UDP网络通信的通道，可以进行发送和接收数据包。

4. `Pipe.SinkChannel`和`Pipe.SourceChannel`：用于两个线程之间的单向管道通信。

`SelectableChannel`提供了以下主要方法来支持通道的选择操作：

- `configureBlocking(boolean block)`：设置通道是否为阻塞模式。

- `register(Selector sel, int ops)`：将通道注册到指定的选择器上，并指定关注的事件类型（如`SelectionKey.OP_READ`、`SelectionKey.OP_WRITE`等）。

- `keyFor(Selector sel)`：返回当前通道已经注册到的选择器。

- `isRegistered()`：判断通道是否已经注册到选择器。

- `blockingLock()`：获取用于同步阻塞和非阻塞模式设置的对象。

- `isBlocking()`：判断通道是否为阻塞模式。

- `close()`：关闭通道。

- `isOpen()`：判断通道是否处于打开状态。

使用`SelectableChannel`配合`Selector`，可以实现高效的非阻塞I/O操作。通过将多个通道注册到一个选择器上，可以在单个线程中管理多个通道的I/O事件，从而减少线程的创建和切换，提高系统的性能和可扩展性。

下面是一个简单的示例，展示如何使用`SelectableChannel`和`Selector`来进行非阻塞的网络通信：

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NonBlockingServerExample {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("非阻塞服务器启动，监听端口：" + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    acceptClientConnection(key);
                } else if (key.isReadable()) {
                    readMessageFromClient(key);
                }
            }
        }
    }

    private static void acceptClientConnection(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ);

        System.out.println("新客户端连接：" + clientChannel.getRemoteAddress());
    }

    private static void readMessageFromClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            // 客户端关闭连接
            key.cancel();
            clientChannel.close();
            return;
        }

        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String message = new String(data).trim();
        System.out.println("收到客户端消息：" + message);
    }
}
```

在这个示例中，我们使用了`ServerSocketChannel`作为服务器端通道，以及`SocketChannel`作为客户端通道。我们将服务器端通道和客户端通道都设置为非阻塞模式，并通过`Selector`对它们进行事件的注册。通过这种方式，我们实现了一个简单的非阻塞的服务器端，可以同时管理多个客户端连接。



# 3 Java Thread

## 3.1 ScheduledExecutorService
`ScheduledExecutorService`是Java中用于创建定时任务和周期性任务的接口。它继承自`ExecutorService`接口，并在其基础上提供了一些额外的方法，使得可以在给定的延迟后执行任务，或者以固定的时间间隔重复执行任务。

`ScheduledExecutorService`通常用于替代`Timer`类，它提供了更加灵活和强大的任务调度功能。相比于`Timer`，`ScheduledExecutorService`的优点在于：

1. **线程安全**：`ScheduledExecutorService`是线程安全的，因此多个线程可以安全地共享一个实例，并且不会造成竞争条件。

2. **异常处理**：`ScheduledExecutorService`可以更好地处理任务的异常情况。当一个任务由于异常而中止时，`ScheduledExecutorService`会捕获并处理该异常，并确保后续的任务继续执行。

3. **配置灵活**：`ScheduledExecutorService`允许更灵活的配置任务的执行时间和频率。可以使用延迟时间或固定的时间间隔来安排任务的执行。

4. **可管理性**：`ScheduledExecutorService`允许您通过`shutdown()`和`shutdownNow()`方法来优雅地关闭任务调度器，确保所有任务都完成并释放相关资源。

`ScheduledExecutorService`接口定义了以下主要方法：

- `schedule(Runnable command, long delay, TimeUnit unit)`：在指定的延迟时间后执行任务。

- `schedule(Callable<V> callable, long delay, TimeUnit unit)`：在指定的延迟时间后执行任务，并返回一个`Future`对象用于获取任务的执行结果。

- `scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)`：以固定的时间间隔执行任务，从指定的初始延迟开始。

- `scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)`：以固定的时间间隔执行任务，从一个任务完成后的延迟时间开始。

要使用`ScheduledExecutorService`，您需要通过`Executors`类的静态方法之一来创建一个`ScheduledExecutorService`的实例。例如，创建一个单线程的`ScheduledExecutorService`可以使用`Executors.newSingleThreadScheduledExecutor()`方法。

下面是一个简单的示例，展示如何使用`ScheduledExecutorService`创建定时任务：

```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 在延迟1秒后执行任务
        scheduler.schedule(() -> System.out.println("任务执行了！"), 1, TimeUnit.SECONDS);

        // 关闭任务调度器
        scheduler.shutdown();
    }
}
```

在这个示例中，我们创建了一个单线程的`ScheduledExecutorService`，并在延迟1秒后执行一个简单的任务，然后关闭了任务调度器。