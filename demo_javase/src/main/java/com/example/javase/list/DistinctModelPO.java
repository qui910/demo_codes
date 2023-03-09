package com.example.javase.list;

import lombok.Data;

import java.util.Objects;

/**
 * 剔重测试对象
 * <p>
 *     根据userName剔重，需要重写hashCode和equals方法。<br>
 *     如果不重写hashCode和equals，则是根据userName+password判断是否重复
 * </p>
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-06 14:57
 * @since 1.8
 **/
@Data
public class DistinctModelPO implements Comparable<DistinctModelPO>{

    private String userName;

    private String passWord;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistinctModelPO that = (DistinctModelPO) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public int compareTo(DistinctModelPO po) {
        if (Objects.equals(userName, po.userName)) {
            return 1;
        } else {
            return -1;
        }
    }
}
