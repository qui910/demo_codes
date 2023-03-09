package com.example.webstomp;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.concurrent.ExecutionException;

/**
 * 控制器代码
 * <p>
 * {@code WsController}注解，表示注册一个Controller，WebSocket的消息处理需要放在Controller下
 *
 * @author pang
 **/
@RestController
@Slf4j
public class WebSocketControllerThree {

    /**
     * Spring WebSocket消息发送模板
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WebSocketClientThree clientThree;

    /**
     * 发送广播通知
     * (1){@code @MessageMapping} 和 {@code @RequestMapping}功能类似，浏览器向服务器发起消息，映射到该地址。
     * (2){@code @MessageMapping("/addNotice") }用于接收客户端发来的消息，客户端发送消息地址为：/app/addNotice，
     * 如果服务器接受到了消息，就会对订阅了{@code @SendTo}括号中的地址的浏览器发送消息。
     * (3){@code @SendTo("/topic/notice")} 用于向客户端发送广播消息（方式一），客户端订阅消息地址为：/topic/notice
     */
    @MessageMapping("/addNotice")
    @SendTo("/topic/notice")
    public WsMessageThree say(String notice, Principal fromUser) throws MessagingException {
        // 业务处理
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName(fromUser.getName());
        msg.setContent(notice);

        if (StrUtil.equals(notice, "error")) {
            throw new MessagingException("测试异常");
        }

        //向客户端发送广播消息（方式二），客户端订阅消息地址为：/topic/notice
//        messagingTemplate.convertAndSend("/topic/notice", msg);
        return msg;
    }

    /**
     * 发送点对点消息
     * {@code @MessageMapping("/msg") } 指明接收客户端发来的消息，客户端发送消息地址为：/app/msg
     * {@code @SendToUser("/queue/msg/result")} 指明向当前发消息客户端（就是自己）发送消息的反馈结果，客户端订阅消息地址为：/user/queue/msg/result
     */
    @MessageMapping("/msg")
    @SendToUser("/queue/msg/result")
    public boolean sendMsg(WsMessageThree message, Principal fromUser) {
        // 业务处理
        message.setFromName(fromUser.getName());

        // 向指定客户端发送消息，第一个参数Principal.name为前面websocket握手认证通过的用户name（全局唯一的），客户端订阅消息地址为：/user/queue/msg/new
        messagingTemplate.convertAndSendToUser(message.getToName(), "/queue/msg/new", message);
        return true;
    }

    @MessageExceptionHandler(MessagingException.class)
    @SendToUser("/queue/errors")
    public String handleExceptions(Throwable t) {
        log.error("Error handing message: {}", t.getMessage());
        return t.getMessage();
    }

    @GetMapping("/sendClientMsg")
    public void sendClientMsg(@RequestParam("message") String message, @RequestParam("toUser") String toUser) throws ExecutionException,
            InterruptedException {
        if (StrUtil.isNotBlank(toUser)) {
            clientThree.sendToUser(toUser, message);
        } else {
            clientThree.sendTopic(message);
        }
    }


    @GetMapping("/openClient")
    public void openClient() throws ExecutionException, InterruptedException {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String fromUser = request.getHeader("token");
        clientThree.openClient(fromUser);
    }
}
