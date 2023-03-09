package com.example.webstomp;

import lombok.Data;

/**
 * 消息处理对象
 *
 * @author pang
 **/
@Data
public class WsMessageThree {
    /**
     * 消息接收人，对应认证用户Principal.name（全局唯一）
     */
    private String toName;
    /**
     * 消息发送人，对应认证用户Principal.name（全局唯一）
     */
    private String fromName;
    /**
     * 消息内容
     */
    private Object content;
}
