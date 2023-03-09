package com.example.javase.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.javase.inner.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * NUll测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-23 11:19
 * @since 1.8
 **/
@Slf4j
public class NullTest {

    @Test
    @DisplayName("测试if中存在null但是有or的情况")
    public void testNullByIfOr() {
        Person person = null;
        if (ObjectUtil.isNull(person) || StrUtil.isBlank(person.getName())) {
            log.info("测试成");
        }
    }
}
