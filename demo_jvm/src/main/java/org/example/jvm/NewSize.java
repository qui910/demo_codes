package org.example.jvm;

/**
 * 新生代配置测试
 */
public class NewSize {
    public static void main(String[] args) {
        byte[] b=null;
        for(int i=0;i<10;i++)
            b=new byte[1*1024*1024];
    }
}
