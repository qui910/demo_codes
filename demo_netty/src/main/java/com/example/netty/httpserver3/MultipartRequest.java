package com.example.netty.httpserver3;

import io.netty.handler.codec.http.multipart.FileUpload;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 10:47
 * @since 1.8
 **/
public class MultipartRequest {
    private Map<String, FileUpload> fileUploads;
    private JSONObject params;
    public Map<String, FileUpload> getFileUploads() {
        return fileUploads;
    }
    public void setFileUploads(Map<String, FileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }
    public JSONObject getParams() {
        return params;
    }
    public void setParams(JSONObject params) {
        this.params = params;
    }
}
