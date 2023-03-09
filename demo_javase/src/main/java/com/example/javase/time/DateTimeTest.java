package com.example.javase.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:45
 * @since 1.8
 **/
@Slf4j
public class DateTimeTest {

    @Test
    public void testSimpleDateTime() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        log.info("时间格式yyyy-MM-dd，Date转字符串:{}",df.format(c.getTime()));

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("时间格式yyyy-MM-dd HH:mm:ss，字符串转Date:{}",df1.parse("2021-12-12 00:00:00"));
    }
}
