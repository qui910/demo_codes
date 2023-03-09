package com.example.webstomp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * 配置Spring启用WebSocket和STOMP消息传递。
 * 实现WebSocketMessageBrokerConfigurer接口，注册一个STOMP节点，配置一个广播消息代理。
 * <p>
 * {@code @Configuration}表示当前类注册为Spring配置类。
 * <p>
 * {@code @EnableWebSocketMessageBroker}注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller，
 * 开始支持@MessageMapping,就像是使用@requestMapping一样。
 * <p>
 * 除了实现WebSocketMessageBrokerConfigurer接口，也可以继承AbstractWebSocketMessageBrokerConfigurer的配置类实现WebSocket
 * 配置，由于WebSocketMessageBrokerConfigurer接口提供了默认实现，所以AbstractWebSocketMessageBrokerConfigurer
 * 已被废弃，这里不建议使用继承AbstractWebSocketMessageBrokerConfigurer的方式配置。
 *
 * @author pang
 **/

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigThree implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册STOMP协议节点并映射url
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/clientws")
                .addInterceptors(new MyHandshakeInterceptorThree())
                .setHandshakeHandler(new WebSocketHandShakeHandlerThree())
                .setAllowedOrigins("*");

        //注册一个Stomp的节点(endpoint),并指定使用SockJS协议。
        registry // 注册一个 /marcopolo的WebSocket端点
                .addEndpoint("/marcopolo")
                // 添加自定义的WebSocket握手拦截器
                .addInterceptors(new MyHandshakeInterceptorThree())
                // 添加内置WebSocket握手拦截器
//                .addInterceptors(new HttpSessionHandshakeInterceptor())
                // 添加WebSocket握手处理器
                .setHandshakeHandler(new WebSocketHandShakeHandlerThree())
                // 设置允许可跨域的域名
                .setAllowedOrigins("*")
                // 指定使用SockJS协议
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置客户端接收消息地址的前缀（可不设置）, 这个消息代理必须和 controller 中的 @SendTo 配置的地址前缀一样或者全匹配
        registry.enableSimpleBroker(
                // 广播消息前缀
                "/topic",
                // 点对点消息前缀
                "/queue");
        // 设置客户端接收点对点消息地址的前缀，默认为 /user
        registry.setUserDestinationPrefix("/user");
        // 设置客户端向服务器发送消息的地址前缀（可不设置）
        registry.setApplicationDestinationPrefixes("/app");

        // 启用STOMP
//        registry.enableStompBrokerRelay("/topic","/queue")
//                .setRelayHost("rabbit.someotherserver")
//                .setRelayPort(62623)
//                .setClientLogin("marcopolo")
//                .setClientPasscode("letmein01");
//        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        return stompClient;
    }
}
