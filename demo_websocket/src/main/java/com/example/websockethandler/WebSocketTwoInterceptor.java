package com.example.websockethandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * WebSocket握手时的拦截器
 *
 * @author pang
 */
@Slf4j
public class WebSocketTwoInterceptor implements HandshakeInterceptor {

    /**
     * 关联HttpSession和WebSocketSession，
     * beforeHandShake方法中的Map参数 就是对应WebSocketSession里的属性
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        // 前置拦截一般用来注册用户信息，绑定WebSocketSession
        if (request instanceof ServletServerHttpRequest) {
            log.info("*****beforeHandshake******");
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpSession session = httpServletRequest.getSession(true);

            log.info("mchNo：{}", httpServletRequest.getParameter("mchNo"));
            if (session != null) {
                attributes.put("sessionId", session.getId());
                attributes.put("mchNo", httpServletRequest.getParameter("mchNo"));
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        // 后置拦截
        log.info("******afterHandshake******");
    }
}
