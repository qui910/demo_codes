package org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * 工具类XMLUtil来读取配置文件中的字符串参数
 * @version 1.0
 * @date 2023-08-09 10:35
 * @since 1.8
 **/
@Slf4j
public class XMLUtil {
    /**
     * 该方法用于从XML配置文件中提取图表类型，并返回类型名
     * @return
     */
    public static String getChartType(String filePath) {
        try {
            //创建文档对象
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            Document doc;
            doc = builder.parse(new File(filePath+"config.xml"));
            //获取包含图表类型的文本节点
            NodeList nl = doc.getElementsByTagName("chartType");
            Node classNode = nl.item(0).getFirstChild();
            String chartType = classNode.getNodeValue().trim();
            return chartType;
        } catch(Exception e) {
            log.error(e.getLocalizedMessage(),e);
            return null;
        }
    }
}
