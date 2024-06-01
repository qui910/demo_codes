package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3;

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
        //读取配置文件中的参数
        String type = XMLUtil.getChartType(this.getClass().getResource("").getPath());
        //创建产品对象
        chart = ChartFactory.getChart(type);
        chart.display();
    }
}
