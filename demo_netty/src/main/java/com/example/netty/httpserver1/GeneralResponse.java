package com.example.netty.httpserver1;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:13
 * @since 1.8
 **/
@Data
public class GeneralResponse {
    private transient HttpResponseStatus status = HttpResponseStatus.OK;
    private String message = "SUCCESS";
    private Object data;

    public GeneralResponse(Object data) {
        this.data = data;
    }

    public GeneralResponse(HttpResponseStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
