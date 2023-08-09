package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo2;

import org.junit.Test;

/**
 * 客户端测试代码
 * @version 1.0
 * @date 2023-08-09 10:11
 * @since 1.8
 **/
public class ChartFactoryTest {

    @Test
    public void testFactory() {
        Chart chart;
        // 通过静态工厂方法创建产品
        chart = ChartFactory.getChart("histogram");
        chart.display();

        chart = ChartFactory.getChart("pie");
        chart.display();

        chart = ChartFactory.getChart("line");
        chart.display();
    }
}
