package com.example.websockethandler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author pang
 * @version 1.0
 * @date 2023-01-06 13:16
 * @since 1.8
 **/
@Slf4j
public class WebSocketTwoHandler extends TextWebSocketHandler implements WebSocketHandler {

    /**
     * 在线用户列表
     */
    private static final Map<String, WebSocketSession> USERS_MAP;
    /**
     * 用户标识
     */
    private static final String CLIENT_ID = "mchNo";

    static {
        USERS_MAP = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("成功建立websocket-spring连接");
        String mchNo = getMchNo(session);
        if (StrUtil.isNotEmpty(mchNo)) {
            USERS_MAP.put(mchNo, session);
            session.sendMessage(new TextMessage("成功建立websocket-spring连接"));
            log.info("用户标识：{}，Session：{}", mchNo, session.toString());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("收到客户端消息：{}", message.getPayload());
        JSONObject msgJson = JSONObject.parseObject(message.getPayload());
        String to = msgJson.getString("to");
        String msg = msgJson.getString("msg");
        WebSocketMessage<?> webSocketMessageServer = new TextMessage("server:" + message);
        try {
            session.sendMessage(webSocketMessageServer);
            if ("all".equals(to.toLowerCase())) {
                sendMessageToAllUsers(new TextMessage(getMchNo(session) + ":" + msg));
            } else {
                sendMessageToUser(to, new TextMessage(getMchNo(session) + ":" + msg));
            }
        } catch (IOException e) {
            log.error("handleTextMessage method error", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.info("连接出错");
        USERS_MAP.remove(getMchNo(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("连接已关闭：{}", status);
        USERS_MAP.remove(getMchNo(session));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送信息(全部用户或指定用户)
     *
     * @param jsonData 信息，格式 {"to":"mchNo","msg":"消息"}。当to为all时，发送给全部用户，否则为指定用户
     */
    public void sendMessage(String jsonData) {
        log.info("收到客户端消息sendMessage：{}", jsonData);
        JSONObject msgJson = JSONObject.parseObject(jsonData);
        String mchNo = StrUtil.isEmpty(msgJson.getString(CLIENT_ID)) ? "陌生人" : msgJson.getString(CLIENT_ID);
        String to = msgJson.getString("to");
        String msg = msgJson.getString("msg");
        if (StrUtil.isBlank(to) || "all".equals(to.toLowerCase())) {
            sendMessageToAllUsers(new TextMessage(mchNo + ":" + msg));
        } else {
            sendMessageToUser(to, new TextMessage(mchNo + ":" + msg));
        }
    }

    /**
     * 发送信息给指定用户
     *
     * @param mchNo   用户标识
     * @param message 信息
     */
    public void sendMessageToUser(String mchNo, TextMessage message) {
        if (USERS_MAP.get(mchNo) == null) {
            return;
        }
        WebSocketSession session = USERS_MAP.get(mchNo);
        log.info("sendMessage：{} ,msg：{}", session, message.getPayload());
        if (!session.isOpen()) {
            log.info("客户端:{},已断开连接，发送消息失败", mchNo);
            return;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("sendMessageToUser method error", e);
        }
    }

    /**
     * 广播信息
     *
     * @param message 信息
     */
    public void sendMessageToAllUsers(TextMessage message) {
        Set<String> mchNos = USERS_MAP.keySet();
        WebSocketSession session;
        for (String mchNo : mchNos) {
            try {
                session = USERS_MAP.get(mchNo);
                if (session.isOpen()) {
                    session.sendMessage(message);
                } else {
                    log.info("客户端:{},已断开连接，发送消息失败", mchNo);
                }
            } catch (IOException e) {
                log.error("sendMessageToAllUsers method error", e);
            }
        }
    }

    /**
     * 获取用户标识
     *
     * @param session 连接会话
     * @return 用户表示
     */
    private String getMchNo(WebSocketSession session) {
        try {
            return session.getAttributes().get(CLIENT_ID).toString();
        } catch (Exception e) {
            return null;
        }
    }
}
