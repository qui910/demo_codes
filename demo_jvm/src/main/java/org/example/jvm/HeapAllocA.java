package org.example.jvm;

/**
 * 初始堆和最大堆
 */
public class HeapAllocA {

    public static void main(String[] args) {
        System.out.print("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory()+" bytes");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()+" bytes");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()+" bytes");

        byte[] b=new byte[2*1024*1024];
        System.out.println("分配了1M空间给数组");
        System.out.print("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory()+" bytes");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()+" bytes");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()+" bytes");

        b=new byte[4*1024*1024];
        System.out.println("分配了4M空间给数组");

        System.out.print("maxMemory=");
        System.out.println(Runtime.getRuntime().maxMemory()+" bytes");
        System.out.print("free mem=");
        System.out.println(Runtime.getRuntime().freeMemory()+" bytes");
        System.out.print("total mem=");
        System.out.println(Runtime.getRuntime().totalMemory()+" bytes");
    }
}
