package com.example.webstomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * Spring STOMP客户端
 *
 * @author pang
 **/
@Service
public class WebSocketClientThree {
    @Autowired
    private WebSocketStompClient stompClient;

    private StompSession stompSession;

    public void openClient(String fromUser) throws ExecutionException, InterruptedException {
        StompSessionHandler sessionHandler = new MySessionHandler();
        // 这里不能使用/marcopolo，因为其有withSockJS
        // 虽然这里连接的是/clientws，而页面连接的是/marcopolo 但是二者是可以通信的
        stompSession = stompClient.connect("ws://127.0.0.1:8081/demo/clientws?token=" + fromUser,
                sessionHandler).get();
    }

    public void sendTopic(String message) {
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName("user4");
        msg.setContent(message);
        stompSession.send("/topic/notice", msg);
    }

    public void sendToUser(String toUser, String message) {
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName("user4");
        msg.setContent(message);
        msg.setToName(toUser);
        stompSession.send("/user/" + toUser + "/queue/msg/new", msg);
    }
}
