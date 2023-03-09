package com.example.webstomp;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

/**
 * @author pang
 **/
@Slf4j
public class MySessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/notice", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WsMessageThree.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("Received Topic: {}", JSONObject.toJSONString(payload));
            }
        });

        session.subscribe("/user/queue/msg/new", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WsMessageThree.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("Received Queue: {}", JSONObject.toJSONString(payload));
            }
        });

        WsMessageThree msg = new WsMessageThree();
        msg.setFromName("user4");
        msg.setContent("我连接了");
        session.send("/topic/notice", msg);

        log.info("New session: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return WsMessageThree.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received: {}", ((WsMessageThree) payload).getContent());
    }
}
