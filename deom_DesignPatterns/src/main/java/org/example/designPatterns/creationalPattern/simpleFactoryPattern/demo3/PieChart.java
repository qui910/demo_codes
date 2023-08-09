package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3;

import lombok.extern.slf4j.Slf4j;

/**
 * 饼状图类：具体产品类
 * @version 1.0
 * @date 2023-08-09 10:03
 * @since 1.8
 **/
@Slf4j
public class PieChart implements Chart {
    public PieChart() {
        log.info("创建饼状图！");
    }
    @Override
    public void display() {
        log.info("显示饼状图！");
    }
}
