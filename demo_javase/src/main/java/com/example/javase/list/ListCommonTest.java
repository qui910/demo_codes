package com.example.javase.list;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * List的一般测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 15:36
 * @since 1.8
 **/
@Slf4j
public class ListCommonTest {

    @Test
    @DisplayName("测试Empty队列ForEach是否异常")
    public void testEmptyForEach() {
        List<String> list = Lists.newArrayList();
        list.forEach(e->{
            log.info("-->正常");
        });
    }
}
