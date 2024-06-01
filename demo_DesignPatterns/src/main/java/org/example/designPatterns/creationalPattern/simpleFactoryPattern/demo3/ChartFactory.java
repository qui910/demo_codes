package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3;

import lombok.extern.slf4j.Slf4j;

/**
 * 图表工厂类：工厂类
 * @version 1.0
 * @date 2023-08-09 10:10
 * @since 1.8
 **/
@Slf4j
public class ChartFactory {
    /**
     * 静态工厂方法
     */
    public static Chart getChart(String type) {
        Chart chart = null;
        if (type.equalsIgnoreCase("histogram")) {
            chart = new HistogramChart();
            log.info("初始化设置柱状图！");
        }
        else if (type.equalsIgnoreCase("pie")) {
            chart = new PieChart();
            log.info("初始化设置饼状图！");
        }
        else if (type.equalsIgnoreCase("line")) {
            chart = new LineChart();
            log.info("初始化设置折线图！");
        }
        return chart;
    }
}
