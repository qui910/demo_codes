package org.example.ex01;

/*
  HTTP Response = Status-Line
    *(( general-header | response-header | entity-header ) CRLF)
    CRLF
    [ message-body ]
    Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
*/

import lombok.extern.slf4j.Slf4j;
import org.example.common.CommonUtils;

import java.io.*;
import java.util.Objects;

import static org.example.common.CommonConstants.BUFFER_SIZE;

@Slf4j
public class Response {

    private final Request request;
    private final OutputStream output;

    public Response(OutputStream output,Request request) {
        this.output = output;
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        output.write(CommonUtils.getStaticResource(request.getUri()));
    }
}