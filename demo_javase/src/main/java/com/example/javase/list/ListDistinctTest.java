package com.example.javase.list;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 队列剔重测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-06 14:47
 * @since 1.8
 **/
@Slf4j
public class ListDistinctTest {

    private static final DistinctModelPO[] DISTINCT_MODEL_POS = new DistinctModelPO[6];

    static {
        DistinctModelPO distinctModelPo1 = new DistinctModelPO();
        distinctModelPo1.setUserName("a1");
        distinctModelPo1.setPassWord("b1");
        DISTINCT_MODEL_POS[0] = distinctModelPo1;

        DistinctModelPO distinctModelPo2 = new DistinctModelPO();
        distinctModelPo2.setUserName("a2");
        distinctModelPo2.setPassWord("b2");
        DISTINCT_MODEL_POS[1] = distinctModelPo2;

        DistinctModelPO distinctModelPo3 = new DistinctModelPO();
        distinctModelPo3.setUserName("a1");
        distinctModelPo3.setPassWord("b1");
        DISTINCT_MODEL_POS[2] = distinctModelPo3;

        DistinctModelPO distinctModelPo4 = new DistinctModelPO();
        distinctModelPo4.setUserName("a2");
        distinctModelPo4.setPassWord("b2");
        DISTINCT_MODEL_POS[3] = distinctModelPo4;

        DISTINCT_MODEL_POS[4] = null;

        DISTINCT_MODEL_POS[5] = null;
    }

    @Test
    @DisplayName("使用LinkedHashSet剔重(有序)")
    public void distinctByLinkedHashSet() {
        LinkedHashSet<DistinctModelPO> hashSet = Sets.newLinkedHashSet(Arrays.asList(DISTINCT_MODEL_POS));
        ArrayList<DistinctModelPO> listWithoutDuplicates = new ArrayList<>(hashSet);
        // 不能去除List中的Null对象
        log.info("使用LinkedHashSet剔重-结果：{}",listWithoutDuplicates);
    }

    @Test
    @DisplayName("使用java8新特性stream剔重(有序)")
    public void distinctByStream() {
        List<DistinctModelPO> listWithoutDuplicates = Arrays.stream(DISTINCT_MODEL_POS).distinct().collect(Collectors.toList());
        // 不能去除List中的Null对象
        log.info("使用java8新特性stream剔重-结果：{}",listWithoutDuplicates);
    }

    @Test
    @DisplayName("使用HashSet不能添加重复数据的特性剔重(无序)")
    public void distinctByHashSet() {
        List<DistinctModelPO> list = Arrays.asList(DISTINCT_MODEL_POS);
        HashSet<DistinctModelPO> set = new HashSet<>(list.size());
        List<DistinctModelPO> result = new ArrayList<>(list.size());
        for (DistinctModelPO po : list) {
            if (set.add(po)) {
                result.add(po);
            }
        }
        // 不能去除List中的Null对象
        log.info("使用HashSet不能添加重复数据的特性剔重-结果：{}",result);
    }

    @Test
    @DisplayName("使用TreeSet剔重(有序)")
    public void distinctByTreeSet() {
        // 需要DistinctModelPO类实现Comparable接口
        // 使用TreeSet时不能传null值，否则会提示错误，所以无法测试
        TreeSet<DistinctModelPO> set = new TreeSet<>(Arrays.asList(DISTINCT_MODEL_POS));
        List<DistinctModelPO> result = new ArrayList<>(set);
        // 不能去除List中的Null对象
        log.info("使用TreeSet剔重-结果：{}",result);
    }

    @Test
    @DisplayName("利用List的contains方法剔重(有序)")
    public void distinctByList() {
        List<DistinctModelPO> list = Arrays.asList(DISTINCT_MODEL_POS);
        List<DistinctModelPO> result = new ArrayList<>(list.size());
        for (DistinctModelPO po : list) {
            if (!result.contains(po)) {
                result.add(po);
            }
        }
        // 不能去除List中的Null对象
        log.info("利用List的contains方法剔重-结果：{}",result);
    }

    @Test
    @DisplayName("双重for循环剔重(有序)")
    public void distinctByFor() {
        List<DistinctModelPO> list = Arrays.asList(DISTINCT_MODEL_POS);
        List<DistinctModelPO> listNew = new ArrayList<>();
        for (DistinctModelPO modelPO : list) {
            if (ObjectUtil.isNull(modelPO)) {
                continue;
            }
            if (CollUtil.isNotEmpty(listNew)) {
                boolean contains = false;
                for (DistinctModelPO distinctModelPO : listNew) {
                    if (ObjectUtil.isNotNull(modelPO) && ObjectUtil.isNotNull(distinctModelPO)
                            && ObjectUtil.equal(modelPO.getUserName(), distinctModelPO.getUserName())
                            && ObjectUtil.equal(modelPO.getPassWord(), distinctModelPO.getPassWord())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    listNew.add(modelPO);
                }
            } else {
                listNew.add(modelPO);
            }
        }
        // 可以去除List中的Null对象
        log.info("双重for循环剔重-结果：{}",listNew);
    }


    @Test
    @DisplayName("使用HuTool工具剔重(有序)")
    public void distinctByHuTool() {
        List<DistinctModelPO> list = CollUtil.distinct(Arrays.asList(DISTINCT_MODEL_POS));
        // 不能去除List中的Null对象
        log.info("使用HuTool工具剔重-结果：{}",list);
    }

    @Test
    @DisplayName("使用ImmutableSet剔重(有序)")
    public void distinctByImmutableSet() {
        List<DistinctModelPO> list = ImmutableSet.copyOf(Iterables.filter(Arrays.asList(DISTINCT_MODEL_POS), Predicates.not(Predicates.isNull()))).asList();
        // 可以去除List中的Null对象
        log.info("使用ImmutableSet剔重-结果：{}",list);
    }

    @Test
    @DisplayName("使用Stream+Map剔重(无序)")
    public void distinctByStreamMap() {
        Map<String, DistinctModelPO> receiveContactInfoMap = Arrays.stream(DISTINCT_MODEL_POS)
                .filter(e -> ObjectUtil.isNotNull(e) && StrUtil.isNotBlank(e.getUserName()) && StrUtil.isNotBlank(e.getPassWord()))
                .collect(Collectors.toMap(e -> e.getUserName() + "_" + e.getPassWord(), e -> e, (v1,v2) -> v1));
        List<DistinctModelPO> result = new ArrayList<>(receiveContactInfoMap.values());
        log.info("使用Stream+Map剔重-结果：{}",result);
    }
}
