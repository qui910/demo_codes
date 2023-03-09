package com.example.webstomp;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author pang
 **/
public class WebSocketHandShakeHandlerThree extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // 设置认证通过的用户到当前会话中
        return (Principal) attributes.get("user");
    }
}
