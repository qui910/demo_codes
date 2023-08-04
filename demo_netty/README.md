[toc]
>记录Netty学习的Demo项目

# 1 第一个Netty应用
这里将建立一个简单的Echo客户端/服务器，其客户端和服务器之间的交互是很简单的。客户端启动后，建立一个连接发送一个或多个消息发送到服务器，其中每相呼应消息返回给客户端。诚然，这个应用程序并不是非常有用。但这项工作是为了更好的理解请求 - 响应交互本身，这是一个基本的模式的客户端/服务器系统。
```bash
Code dir = com.example.netty.first
```

## 1.1 通过`ChannelHandler`来实现服务器的逻辑
```bash
com.example.netty.first.EchoServerHandler
```

关键点要牢记：
- ChannelHandler 是给不同类型的事件调用
- 应用程序实现或扩展 ChannelHandler 挂接到事件生命周期和提供自定义应用逻辑。

## 1.2 引导服务器
了解到业务核心处理逻辑 EchoServerHandler 后，下面要引导服务器自身了。
```bash
com.example.netty.first.EchoServer
```

## 1.3 用`ChannelHandler`实现客户端逻辑
用`SimpleChannelInboundHandler`来处理所有的任务，需要覆盖三个方法：
- channelActive() - 服务器的连接被建立后调用
- channelRead0() - 数据后从服务器接收到调用
- exceptionCaught() - 捕获一个异常时调用
```bash
com.example.netty.first.EchoClientHandler
```

SimpleChannelInboundHandler vs. ChannelInboundHandler 何时用这两个要看具体业务的需要？？

在客户端，当`channelRead0`完成，我们已经拿到的入站的信息。当方法返回时，SimpleChannelInboundHandler 会小心的释放对 ByteBuf（保存信息） 的引用。而在 EchoServerHandler
,我们需要将入站的信息返回给发送者，由于 write() 是异步的，在 channelRead() 返回时，可能还没有完成。所以，我们使用 ChannelInboundHandlerAdapter,无需释放信息。最后在 channelReadComplete() 我们调用 ctxWriteAndFlush() 来释放信息。

## 1.4 引导客户端
```bash
com.example.netty.first.EchoClientHandler
```


# 重要类结构

# EventLoopGroup
当谈到Netty时，EventLoopGroup是一个非常重要的概念。它是Netty中用于处理事件循环的一组线程，它负责处理输入数据、执行业务逻辑、处理I/O操作和线程调度等任务。

EventLoopGroup有两种类型：
- NioEventLoopGroup：这是最常用的EventLoopGroup实现，基于Java的NIO（New I/O）库。它使用了Java的NIO特性，如选择器（Selector），来实现高性能的I/O操作。
- OioEventLoopGroup：这种类型使用旧版的Java I/O（Blocking I/O），不推荐在高性能需求的场景中使用。

EventLoopGroup通常会根据你的需要，创建多个EventLoop（也称为EventLoop线程）。每个EventLoop都负责处理一个Channel的所有I/O事件，例如读取、写入、连接和关闭等。

对于服务端，通常会有两个EventLoopGroup：
- Boss Group：这个组负责接受客户端的连接请求，并将连接分配给Worker Group中的某个EventLoop进行处理。Boss Group的EventLoop通常比Worker Group的EventLoop数量要少。
- Worker Group：这个组负责处理已经建立的连接，处理数据的读写和业务逻辑。Worker Group的EventLoop数量通常是根据实际业务负载来配置的。

对于客户端，一般只需要一个EventLoopGroup。

使用EventLoopGroup的好处是，它可以帮助你在单个线程中处理多个Channel的I/O事件，而不需要为每个Channel分配一个独立的线程，从而节省资源并提高性能。
```java
EventLoopGroup bossGroup = new NioEventLoopGroup(); // 创建Boss Group，用于接受客户端连接
EventLoopGroup workerGroup = new NioEventLoopGroup(); // 创建Worker Group，用于处理连接的I/O事件和业务逻辑

try {
    // 创建ServerBootstrap或Bootstrap，并配置EventLoopGroup
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bossGroup, workerGroup)
                  .channel(NioServerSocketChannel.class) // 指定服务器使用NIO传输
                  .childHandler(new YourChannelInitializer()); // 自定义ChannelInitializer用于配置ChannelPipeline

    // 绑定端口并开始接收连接
    ChannelFuture future = serverBootstrap.bind(8080).sync();

    // 等待服务器端口关闭
    future.channel().closeFuture().sync();
} finally {
    // 关闭EventLoopGroup并释放所有资源
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
}
```
需要注意的是，EventLoopGroup在使用完毕后需要调用shutdownGracefully()方法来优雅地关闭，以确保资源得到正确释放。
