package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3;

import lombok.extern.slf4j.Slf4j;

/**
 * 折线图类：具体产品类
 * @version 1.0
 * @date 2023-08-09 10:06
 * @since 1.8
 **/
@Slf4j
public class LineChart implements Chart {
    public LineChart() {
        log.info("创建折线图！");
    }
    @Override
    public void display() {
        log.info("显示折线图！");
    }
}
