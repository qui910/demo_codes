package com.example.javase.stream.flatMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-14 11:15
 * @since 1.8
 **/
@AllArgsConstructor
@Data
public class ToBean {

    private String name;

    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToBean toBean = (ToBean) o;
        return Objects.equals(name, toBean.name) &&
                Objects.equals(password, toBean.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    @Override
    public String toString() {
        return "ToBean{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                // 内存地址
                ", id='" + System.identityHashCode(this) + '\'' +
                ", hashCode='" + this.hashCode() + '\'' +
                '}';
    }
}
