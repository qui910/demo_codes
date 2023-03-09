package com.example.websockethandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket的配置类
 *
 * @author pang
 * @version 1.0
 * @date 2023-01-06 14:14
 * @since 1.8
 **/
@Configuration
@EnableWebSocket //用于开启注解接收和发送消息
public class WebSocketTwoConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // addHandler --> 设置连接路径和处理
        // addInterceptors --> 设置拦截器
        // setAllowedOrigins("*") 一定要加上，不然只有访问localhost，其他的不予许访问
        // setAllowedOrigins(String[] domains),允许指定的域名或IP(含端口号)建立长连接，如果只允许自家域名访问，这里轻松设置。
        // 如果不限时使用"*"号，如果指定了域名，则必须要以http或https开头
        registry.addHandler(webSocketTwoHandler(), "/ws/serverTwo")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketTwoInterceptor());

        // 使用withSockJS()的原因: 一些浏览器中缺少对WebSocket的支持,因此，回退选项是必要的，而Spring框架提供了基于SockJS协议的透明的回退选项。
        // 除此之外，spring也对socketJS提供了支持。
        // 如果代码中添加了withSockJS()如下，服务器也会自动降级为轮询。
        // registry.addEndpoint("/coordination").withSockJS();
        // SockJS的目标是让应用程序使用WebSocket API，但在运行时需要在必要时返回到非WebSocket替代，即无需更改应用程序代码。
        // SockJS是为在浏览器中使用而设计的。它使用各种各样的技术支持广泛的浏览器版本。对于SockJS传输类型和浏览器的完整列表，可以看到SockJS客户端页面。
        // 传输分为3类:WebSocket、HTTP流和HTTP长轮询（按优秀选择的顺序分为3类）
        registry.addHandler(webSocketTwoHandler(), "/sockjs/ws/serverTwo")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketTwoInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler webSocketTwoHandler() {
        return new WebSocketTwoHandler();
    }

}
