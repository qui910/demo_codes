package com.example;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-01-06 09:02
 * @since 1.8
 **/

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 注解 {@code @RestController} 如果使用@RestController注解，会默认将index识别为字符串解析到页面！
 * 我们需要的是index.html页面，并不是字符串
 */
@Controller
public class ViewController {

    @RequestMapping("/index")
    public String index(Map<String, Object> paramMap) {
        // 默认的map的内容会放到请求域中，页面可以直接取值
        paramMap.put("name", "张三");
        paramMap.put("age", 28);
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "index";
    }

    @RequestMapping("/websocket")
    public ModelAndView websocket(ModelAndView mv) {
        mv.setViewName("/websocket");
        mv.addObject("title", "欢迎使用 WebSocketOne!");
        return mv;
    }

    @RequestMapping("/websockettwo")
    public ModelAndView websocketTwo(ModelAndView mv) {
        mv.setViewName("/websockettwo");
        mv.addObject("title", "欢迎使用 WebSocketTwo!");
        return mv;
    }

    @GetMapping("/websocketthree")
    public String getView() {
        return "websocketthree";
    }
}