package org.example.jvm;

import java.util.Vector;

/**
 * 堆溢出示例
 */
public class DumpOOMTest {
    public static void main(String[] args) {
        Vector v = new Vector();
        for(int i = 0; i < 25; i++)
            v.add(new byte[1*1024*1024]);
    }
}
