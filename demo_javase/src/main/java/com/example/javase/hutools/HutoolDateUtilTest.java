package com.example.javase.hutools;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-15 14:50
 * @since 1.8
 **/
@Slf4j
public class HutoolDateUtilTest {

    @Test
    public void testSysdate() {
        log.info("sysdate-->{}", DateUtil.parse(DateUtil.now(),"yyyy-MM-dd HH"));
    }
}
