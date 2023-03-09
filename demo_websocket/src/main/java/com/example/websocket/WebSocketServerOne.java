package com.example.websocket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ws服务端
 * <p>
 * 注解{@code @ServerEndpoint}可以将类定义成一个WebSocket服务器端
 * </p>
 *
 * @author pang
 */
@ServerEndpoint(value = "/ws/serverOne/{sid}")
@Component
@Slf4j
public class WebSocketServerOne {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * 旧：concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。由于遍历set费时，改用map优化
     * private static CopyOnWriteArraySet<WebSocketServerOne> webSocketSet = new
     * CopyOnWriteArraySet<WebSocketServerOne>();
     * 新：使用map对象优化，便于根据sid来获取对应的WebSocket
     */
    private static final ConcurrentHashMap<String, WebSocketServerOne> WEBSOCKET_MAP = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收用户的sid，指定需要推送的用户
     */
    private String sid;

    /**
     * 连接成功后调用的方法
     * <p>
     * 这里{@code @OnOpen}注解，表示有浏览器链接过来的时候被调用
     * </p>
     *
     * @param session 连接会话
     * @param sid     用户ID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        // 加入map中
        WEBSOCKET_MAP.put(sid, this);
        // 在线数加 1
        addOnlineCount();
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        sendMessage("连接成功");
    }

    /**
     * 连接关闭调用的方法
     * <p>
     * 这里{@code @OnClose}注解，表示浏览器发出关闭请求的时候被调用
     * </p>
     */
    @OnClose
    public void onClose() {
        if (WEBSOCKET_MAP.get(this.sid) != null) {
            // 从map中删除
            WEBSOCKET_MAP.remove(this.sid);
            // 在线数减 1
            subOnlineCount();
            log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用的方法，根据业务要求进行处理，这里就简单地将收到的消息直接群发推送出去
     * <p>
     * 这里{@code @OnMessage}注解，表示浏览器发消息的时候被调用<br>
     * maxMessageSize:用来指定消息字节最大限制，超过限制就会关闭连接，文字聊天不设置基本没什么问题，默认的大小够用了，
     * 传图片或者文件可能就会因为超出限制而导致连接“莫名其妙”被关闭，这个坑还是比较难发现的。
     * </p>
     *
     * @param message 客户端发送过来的消息
     * @param session 连接会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        if (StrUtil.isNotBlank(message)) {
            String toSid = JSONObject.parseObject(message).getString("toUserId");
            String contentText = JSONObject.parseObject(message).getString("contentText");
            WebSocketServerOne toServer = WEBSOCKET_MAP.get(toSid);
            if (ObjectUtil.isNotNull(toServer)) {
                toServer.sendMessage(toSid + "回复" + sid + "的信息：[" + contentText + "] " + DateUtil.formatDateTime(new Date()));
            } else {
                // 全部回复
                for (WebSocketServerOne server : WEBSOCKET_MAP.values()) {
                    server.sendMessage("全部回复" + sid + "的信息：[" + contentText + "] " + DateUtil.formatDateTime(new Date()));
                }
            }
        }
    }

    /**
     * 发生错误时的回调函数
     * <p>
     * 这里{@code @OnError}注解，表示报错了
     * </p>
     *
     * @param session 连接会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送消息
     */
    public void sendMessage(String message) {
        try {
            // 阻塞
            this.session.getBasicRemote().sendText(message);
            // 异步
            // this.session.getAsyncRemote().sendText(message);
        } catch (IOException e) {
            log.error("推送消息失败", e);
        }
    }


    /**
     * 群发自定义消息（用set会方便些）
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        if (StrUtil.isNotBlank(message)) {
            for (WebSocketServerOne server : WEBSOCKET_MAP.values()) {
                // sid为null时群发，不为null则只发一个
                if (sid == null) {
                    server.sendMessage(message);
                } else if (server.sid.equals(sid)) {
                    server.sendMessage(message);
                }
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return ONLINE_COUNT.get();
    }

    public static synchronized void addOnlineCount() {
        ONLINE_COUNT.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        ONLINE_COUNT.decrementAndGet();
    }
}