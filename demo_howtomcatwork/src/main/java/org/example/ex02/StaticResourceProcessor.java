package org.example.ex02;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class StaticResourceProcessor {

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
           log.error("Static Resource Processor error:",e);
        }
    }
}
