package com.example.javase.regexp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pang
 * @version 1.0
 * @date 2023-08-25 10:20
 * @since 1.8
 **/
@Slf4j
public class TestRegexp {

    @Test
    public void testRegexp() {
        Pattern p= Pattern.compile("qtjd|kszr|htqd");
        Matcher m=p.matcher("qtjd1");
        log.info("匹配结果：{}",m.matches()?"是":"否");
    }
}
