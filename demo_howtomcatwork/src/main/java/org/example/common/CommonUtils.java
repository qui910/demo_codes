package org.example.common;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.example.common.CommonConstants.*;

/**
 * 常用工具类
 */
@Slf4j
public class CommonUtils {

    /**
     * 获取静态资源
     * @param path 静态资源路径
     * @return 静态资源字符串
     */
    public static byte[] getStaticResource(String path)
    {
        String filePath = CommonConstants.WEB_ROOT + path;
        File file = new File(filePath);
        if (!file.exists())
        {
            log.error("静态资源不存在:{}",filePath);
            return HTTP_RESPONSE_HEADER_404.getBytes();
        }
        StringBuilder response = new StringBuilder(HTTP_RESPONSE_HEADER);
        if (StrUtil.endWith(path,".html")) {
            response = new StringBuilder(HTTP_RESPONSE_HEADER);
        } else if (StrUtil.endWith(path,".gif")) {
            response = new StringBuilder(HTTP_RESPONSE_IMAGE_GIF_HEADER);
        } else if (StrUtil.endWith(path,".ico")) {
            response = new StringBuilder(HTTP_RESPONSE_IMAGE_ICON_HEADER);
        }

        byte[] data = new byte[(int) file.length()];
        int len;
        try (FileInputStream fis = new FileInputStream(file)) {
           len = fis.read(data);
        } catch (IOException e) {
            log.error("读取静态资源失败",e);
            return HTTP_RESPONSE_HEADER_404.getBytes();
        }

        response.append("Content-Length: ").append(len).append("\r\n");
        response.append("\r\n");

        byte[] result = new byte[response.length() + len];
        System.arraycopy(response.toString().getBytes(),0,result,0,response.length());
        System.arraycopy(data,0,result,response.length(),len);

        return result;
    }
}
