package com.example.javase.inner;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:40
 * @since 1.8
 **/
@Slf4j
public class InnerClassTest {

    @Test
    public void testExtend() {
        log.info("测试内部类继承和覆盖");
        Person pa = new PersonA();
        pa.doWork();
        Person pb = new PersonB();
        pb.doWork();
    }
}
