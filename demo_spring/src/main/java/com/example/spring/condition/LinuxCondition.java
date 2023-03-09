package com.example.spring.condition;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Linux系统环境
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-07 15:02
 * @since 1.8
 **/
public class LinuxCondition implements Condition {

    public static final String LINUX_OS_NAME = "Linux";

    @Override
    public boolean matches(ConditionContext conditionContext, @NotNull AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        String property = environment.getProperty("os.name");
        assert property != null;
        return property.contains(LINUX_OS_NAME);
    }
}
