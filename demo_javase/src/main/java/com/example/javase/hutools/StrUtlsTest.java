package com.example.javase.hutools;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author pang
 * @version 1.0
 * @date 2024-03-26 09:00
 * @since 1.8
 **/
@Slf4j
public class StrUtlsTest {

    @Test
    public void testFormat() {
        log.info("---------{}", StrUtil.format("%06d",1));
    }
}
