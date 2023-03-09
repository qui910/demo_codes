package com.example.webstomp;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

/**
 * WebSocket握手拦截器
 * <p>
 * 可做一些用户认证拦截处理
 *
 * @author pang
 **/
public class MyHandshakeInterceptorThree implements HandshakeInterceptor {
    /**
     * WebSocket握手连接前
     *
     * @return 返回是否同意握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        // 通过url的query参数获取认证参数
        String token = req.getServletRequest().getParameter("token");
        // 根据token认证用户，不通过返回拒绝握手
        Principal user = authenticate(token);
        if (user == null) {
            return false;
        }
        // 保存认证用户
        attributes.put("user", user);
        return true;
    }

    /**
     * WebSocket握手连接后
     */
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }

    /**
     * 根据token认证授权
     */
    private Principal authenticate(String token) {
        // 实现用户的认证并返回用户信息，如果认证失败返回 null
        // 用户信息需继承Principal并实现getName()方法，返回全局唯一值
        LoginPrincipal loginUser = null;
        if (ArrayUtil.contains(new String[]{"all", "user1", "user2", "user3", "user4"}, token)) {
            loginUser = new LoginPrincipal(token);
        }
        return loginUser;
    }
}
