package com.example.javase.io.nio.components;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * NIO缓冲区测试
 * （一）缓冲区Buffer:在Java NIO中负责数据的存取，缓冲区就是数组，用于存储不同数据类型的数据。
 * 根据数据类型不同（boolean）除外，提供了响应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * <p>
 * 上述缓冲区的管理方式几乎一致，通过allocate()获取缓冲区
 * <p>
 * （二）缓冲区存取数据的两个核心方法
 * put() 存入数据到缓冲区
 * get() 获取缓冲区中的数据
 * <p>
 * （三）缓冲区中的四个核心属性：
 * capacity:容量，表示缓冲区中最大存储数据的容量，一旦声明不能修改。
 * limit：界限，表示缓冲区中可以操作数据的大小 （limit后数据不能进行读写）。
 * position：位置，表示缓冲区中正在操作数据的位置。
 * <p>
 * mark ：标记，表示记录当前position的位置，可以通过reset() 恢复到mark的位置
 * <p>
 * 0<=mark <= position <= limit <=capacity
 * <p>
 * (五) 直接缓冲区与非直接缓冲区
 * 非直接缓冲区：通过allocatte()方法分配缓冲区，将缓冲区建立在JVM内存中。
 * 直接缓冲区：通过allocateDirect()方法分配直接缓冲区，将缓冲区建立在物理内存中，可以提高效率。（缺点：不安全；
 * 内存消耗增大，因为写入到内存后数据回收无法控制，只有JVM中与数据相关的引用断开并且GC后，操作系统才会考虑回收这个内存；
 * 且写入数据后JVM无法管理，具体何时再从内存写入到磁盘中无法控制。一般是大数据，或者是可以长期在内存中保留的数据才使用这种方式）
 */
public class NIOBufferTest {

    @Test
    public void testByteBuffer() {
        String str = "abcde";

        // 1 分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("------容器为空时：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());

        // 2 利用put()存入数据到缓冲区
        buf.put(str.getBytes());
        System.out.println("------写数据模式：容器存取5个字符时：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());

        // 3 利用flip()切换为读取数据模式
        buf.flip();
        System.out.println("------切换为读模式后：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());

        // 4 利用get()读取缓冲区中的数据
        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        System.out.println("读取的数据为:" + new String(dst));
        System.out.println("------读数据模式：容器读取全部个字符时：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());

        // 5 利用rewind()可重复读数据
        buf.rewind();
        System.out.println("------切换为重读读模式后：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());
        buf.get(dst);
        System.out.println("读取的数据为:" + new String(dst));

        // 6 利用clear() 清空缓冲区，但是缓冲区中的数据依然存在，但是处于“被遗忘状态”
        buf.clear();
        System.out.println("------清空缓冲区，切换为写模式后：");
        System.out.println("position:" + buf.position());
        System.out.println("limit:" + buf.limit());
        System.out.println("capacity:" + buf.capacity());

        System.out.println("这时存在的遗忘数据:" + (char) buf.get());
    }

    @Test
    public void testByteBufferMarl() {
        String str = "abcde";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();
        // 只读取一部分数据
        byte[] bytes = new byte[2];
        buf.get(bytes);
        System.out.println("读取前2个数据:" + new String(bytes));

        // mark() 标记
        buf.mark();

        buf.get(bytes);
        System.out.println("读取后2个数据:" + new String(bytes));
        System.out.println("position:" + buf.position());

        // reset()恢复到mark的位置
        buf.reset();
        System.out.println("reset 后position:" + buf.position());

        // 判断缓冲区中是否还有剩余数据
        if (buf.hasRemaining()) {
            // 获取缓存区中还可以操作的数量
            System.out.println("缓存区中还可以操作的数量:" + buf.remaining());
        }
    }

    @Test
    public void testDirectByteBuffer() {
        // 分配直接缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);

        System.out.println("是否是直接缓冲区：" + buf.isDirect());
    }
}

