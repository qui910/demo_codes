package com.example.javase.io.nio.components;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * （一）通道（Channel）用于源节点与目标节点的连接，在Java NIO中负责缓冲区中数据的传输。
 * Channel本身不存储数，因此需要配合缓冲区进行传输。
 * （二）通道的主要实现类
 * java.nio.channel.Channel 接口
 * FileChannel  (操作本地文件)
 * SocketChannel   (用于TCP)
 * ServerSocketChannel (用于TCP)
 * DatagramChannel  (用于UDP)
 * （三）获取通道
 * 1. Java针对支持通道的类提供了getChannel() 方法
 * 本地IO：
 * FileInputStream/FileOutputStream
 * RandomAccessFile
 * <p>
 * 网络IO:
 * Socket
 * ServerSocket
 * DatagramSocket
 * <p>
 * 2. 在JDK1.7中的NIO2 针对各个通道提供了静态方法open()
 * <p>
 * 3. 在JDK1.7中的NIO2 的Files工具栏的newByteChannel()
 * <p>
 * （四）通道之间的数据传输
 * transferFrom()
 * transferTo()
 * <p>
 * (五) 分散（Scatter）与聚集（Gather）
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * <p>
 * (六) 字符集：Charset
 * 编码：字符串->字节数组
 * 解码：字节数组->字符串
 */
public class NIOChannelTest {

    // 1 利用通道完成文件的赋值(非直接缓冲区)
    @Test
    public void copyFileByChannelHeap() throws IOException {
        FileInputStream in = new FileInputStream("1.jpg");
        FileOutputStream out = new FileOutputStream("2.jpg");

        // 获取通道
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        // 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 将通道中的数据存入缓冲区
        while (inChannel.read(buf) != -1) {
            //切换读取数据的模式
            buf.flip();
            //将缓冲区中的数据写入通道中
            outChannel.write(buf);
            //情况缓冲区
            buf.clear();
        }

        outChannel.close();
        inChannel.close();
        out.close();
        in.close();
    }

    // 2 使用直接缓冲区完成文件的复制(内存映射文件)
    // 这种方式直接使用内存，即使在完成文件复制后，内存也不会及时释放，程序无法及时退出，会导致主机内存消耗殆尽
    @Test
    public void copyFileByChannelMemory() throws IOException {
        FileChannel inChannel = FileChannel.open(
                Paths.get("1.jpg"), StandardOpenOption.READ);

        // CREATE 2.jpg存在与否都不影响
        // CREATE_NEW 如果2.jpg存在，则报错。
        // 这里除了WRITE，也必须添加READ，因为MappedByteBuffer中的MapMode 只有读写模式，如果这里不加读
        // 则运行报错 java.nio.channels.NonReadableChannelException
        FileChannel outChannel = FileChannel.open(
                Paths.get("2.jpg"), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE, StandardOpenOption.READ);

        // 内存映射映射文件 （原理与allocateDirect一致）
        MappedByteBuffer inMap = inChannel.map(
                FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMap = outChannel.map(
                FileChannel.MapMode.READ_WRITE, 0, outChannel.size());

        // 直接对缓冲区进行数据的读写操作
        byte[] bytes = new byte[inMap.limit()];
        inMap.get(bytes);
        outMap.put(bytes);

        outChannel.close();
        inChannel.close();
    }

    // 通道之间的数据传输（直接缓冲区）
    @Test
    public void copyFileByChannelTrans() throws IOException {
        FileChannel inChannel = FileChannel.open(
                Paths.get("1.jpg"), StandardOpenOption.READ);

        FileChannel outChannel = FileChannel.open(
                Paths.get("2.jpg"), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE, StandardOpenOption.READ);

//        inChannel.transferTo(0,inChannel.size(),outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    // 分散聚集
    @Test
    public void copyFileByScatter() throws IOException {
        RandomAccessFile rm = new RandomAccessFile("1.jpg", "rw");

        // 获取通道
        FileChannel channel = rm.getChannel();

        // 分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel.read(bufs);

        for (ByteBuffer buffer : bufs) {
            buffer.flip();
        }
        System.out.println(buf1.limit());
        System.out.println(buf2.limit());

        // 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("3.jpg", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);

        rm.close();
        raf2.close();
    }

    // 字符集，查看当前可用字符串
    @Test
    public void getCharset() {
        Map<String, Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> set = map.entrySet();

        for (Map.Entry<String, Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    // 测试字符集使用
    // 通过GBK将Char数组编码转换为 Byte数组，
    // 解码是使用GBK将Byte数组转换为Char数组
    @Test
    public void testCharset() throws CharacterCodingException {
        Charset cs1 = Charset.forName("GBK");

        //获取编码器
        CharsetEncoder ce = cs1.newEncoder();

        //获取解码器
        CharsetDecoder cd = cs1.newDecoder();

        CharBuffer cbuf = CharBuffer.allocate(1024);
        cbuf.put("测试中文");
        cbuf.flip();

        //编码
        ByteBuffer buffer = ce.encode(cbuf);
        for (int i = 0; i < buffer.limit(); i++) {
            System.out.println(buffer.get());
        }

        //解码
        buffer.flip();
        CharBuffer cb = cd.decode(buffer);
        System.out.println(cb.toString());

    }
}



