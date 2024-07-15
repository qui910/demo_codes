package com.example.demo_io.rmi;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pang
 * @version 1.0
 * @date 2024-07-15 15:23
 * @since 1.8
 **/
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -7210895257077328551L;

    private String userName;

    private String userDesc;

    private Integer userAge;

    private Boolean userSex;
}
