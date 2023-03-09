package com.example.webstomp;

import cn.hutool.core.util.StrUtil;

import java.security.Principal;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-01-10 11:01
 * @since 1.8
 **/
public class LoginPrincipal implements Principal {

    private final String name;

    public LoginPrincipal(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object another) {
        Principal principal = (Principal) another;
        return StrUtil.equals(name, principal.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String getName() {
        return name;
    }
}
