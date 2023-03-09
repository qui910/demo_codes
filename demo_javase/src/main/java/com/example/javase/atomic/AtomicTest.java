package com.example.javase.atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 原子类测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:13
 * @since 1.8
 **/
@Slf4j
public class AtomicTest {

    /**
     * 测试Java CAS原理
     * compare and swap
     */
    @Test
    public void testCas() {
        AtomicBoolean b = new AtomicBoolean(false);
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
            b.set(true);
            log.info("等线程执行完成后，当前值为:{}",b.get());
        }).start();

        if (b.compareAndSet(true,true)) {
            log.info("CAS设置成功，当前值:{}",b.get());
        } else {
            log.info("CAS设置失败，当前值:{}",b.get());
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }
}
