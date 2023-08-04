package com.example.javase.io.nio.components;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.lang.System.out;

/**
 * NIO，BIO文件复制效率测试
 */
public class NIOFileCopyTest {

    /**
     * 測試使用BIO 缓存流进行复制文件
     */
    @Test
    public void testBIOBufferTime() {
        try (
                BufferedInputStream inBuffer = new BufferedInputStream(new FileInputStream("E:\\a.zip"));
                BufferedOutputStream outBuffer = new BufferedOutputStream(new FileOutputStream("D:\\b.zip"));
        ) {
            int len = 0;
            byte[] bs = new byte[1024];
            long begin = System.currentTimeMillis();
            while ((len = inBuffer.read(bs)) != -1) {
                outBuffer.write(bs, 0, len);
            }
            // 平均时间约 24394 多毫秒
            out.println("复制文件所需的时间：" + (System.currentTimeMillis() - begin));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * NIO文件复制方式之一：
     * 通道之间的数据传输(直接缓冲区的模式)
     */
    @Test
    public void testNIOByTransferFrom() throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get("E:\\a.zip"), StandardOpenOption.READ);
        FileChannel outChennel = FileChannel.open(Paths.get("D:\\b.zip"), StandardOpenOption.WRITE,
                StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);
//        outChennel.transferFrom(inChannel,0,inChannel.size());

        // 加个while循环，保证全部数据都拷贝完成
        long transferred = 0; //已拷贝的字节数
        while (transferred != inChannel.size()) {
            transferred += inChannel.transferTo(0, inChannel.size(), outChennel);
        }
        long end = System.currentTimeMillis();
        // 复制文件所需的时间：29362
        System.out.println("复制文件所需的时间：" + (end - startTime));
    }

    /**
     * NIO文件复制方式之二：
     * 使用直接缓冲区完成文件的复制(内存映射文件)
     * 因这时完全使用内存复制，本机内存不足，报错。
     */
    @Test
    public void testNIOByMappedByteBuffer() throws IOException {
        long startTime = System.currentTimeMillis();

        FileChannel inChannel = FileChannel.open(Paths.get("E:\\a.zip"), StandardOpenOption.READ);
        FileChannel outChennel = FileChannel.open(Paths.get("D:\\b.zip"), StandardOpenOption.WRITE,
                StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        //内存映射文件(什么模式 从哪开始 到哪结束)
        MappedByteBuffer inMappeBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappeBuf = outChennel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接都缓冲区进行数据的读写操作
        byte[] dst = new byte[inMappeBuf.limit()];
        inMappeBuf.get(dst);
        outMappeBuf.put(dst);

        inChannel.close();
        outChennel.close();
        long end = System.currentTimeMillis();
        System.out.println("复制文件所需的时间：" + (end - startTime));
    }


    /**
     * NIO文件复制方式之三：
     * 非直接缓冲区 文件的复制
     *
     * @throws IOException
     */
    @Test
    public void testNIOByChannel() throws IOException {
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\a.zip"));
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\b.zip");

        //获取通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChanel = fileOutputStream.getChannel();

        //分配缓冲区的大小
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //将通道中的数据存入缓冲区
        while (inChannel.read(buf) != -1) {
            buf.flip();//切换读取数据的模式
            while (buf.hasRemaining()) {
                outChanel.write(buf);
            }
            buf.clear();
        }
        outChanel.close();
        inChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        // 复制文件所需的时间：24915
        System.out.println("复制文件所需的时间：" + (end - startTime));
    }
}
