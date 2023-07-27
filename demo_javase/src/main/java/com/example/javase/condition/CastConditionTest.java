package com.example.javase.condition;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Test for Switch Cast Condition
 * @version 1.0
 * @date 2023-04-25 14:42
 * @since 1.8
 **/
@Slf4j
public class CastConditionTest {

    @Test
    public void testCaseWithOutBreak() throws Exception {
        String type = "1401";
        testCaseType(type);
    }

    public String testCaseType(String type) throws Exception {
        switch (type){
            case "26":
                log.info("type 26");
                return "26";
            case "1401":
            case "1402":
                log.info("type 1402");
                return "1402";
            case "1403":
                log.info("type 1403");
                return "1403";
            default:
                log.info("type default");
                throw new Exception();
        }
    }
}
