package com.example.demo_io.rmi;

import lombok.extern.slf4j.Slf4j;

import java.rmi.Naming;
import java.util.List;

/**
 * 客户端调用RMI测试
 *
 */
@Slf4j
public class RemoteClient {

    public static void main(String[] args) throws Exception {
        // 您看，这里使用的是java名称服务技术进行的RMI接口查找。
        RemoteServiceInterface remoteServiceInterface = (RemoteServiceInterface) Naming.lookup("rmi://192.168.61.1/queryAllUserinfo");
        List<UserInfo> users = remoteServiceInterface.queryAllUserinfo();

        log.info("users.size() = " +users.size());
    }
}
