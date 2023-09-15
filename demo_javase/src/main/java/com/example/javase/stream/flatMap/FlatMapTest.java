package com.example.javase.stream.flatMap;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pang
 * @version 1.0
 * @date 2023-09-14 11:16
 * @since 1.8
 **/
@Slf4j
public class FlatMapTest {

    /**
     * 测试通过flatmap将fromBeanList列表中部分字段填充重toBeanList。
     * 匹配规则是 name相同。
     */
    @Test
    public void testCopyListToList() {
        List<FromBean> fromBeanList = Lists.newArrayList();
        fromBeanList.add(new FromBean("a","1"));
        fromBeanList.add(new FromBean("b","2"));
        List<ToBean> toBeanList = Lists.newArrayList();
        toBeanList.add(new ToBean("b",""));
        toBeanList.add(new ToBean("c",""));

        List<ToBean> result = fromBeanList.stream().flatMap(fromBean->toBeanList.stream().map(toBean->{
            if (StrUtil.equals(fromBean.getName(),toBean.getName())) {
                toBean.setPassword(fromBean.getPassword());
            }
            return toBean;
        }).filter(Objects::nonNull)).collect(Collectors.toList());
        // 有重复数据
        log.info(result.toString());
        // 剔重，bean对象中重写hashcode和equals方法
        result.stream().distinct().collect(Collectors.toList()).forEach(System.out::println);
    }
}
