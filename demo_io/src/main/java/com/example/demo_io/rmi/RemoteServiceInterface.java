package com.example.demo_io.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI 服务接口
 * @author pang
 * @version 1.0
 * @date 2024-07-15 15:23
 * @since 1.8
 **/
public interface RemoteServiceInterface extends Remote {
    /**
     * 这个RMI接口负责查询目前已经注册的所有用户信息
     */
    List<UserInfo> queryAllUserinfo() throws RemoteException;
}
