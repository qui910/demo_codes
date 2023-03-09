package com.example.spring.anno;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component //声明组件
@Aspect //声明切面
public class SpringAspectJ {

    // 声明切点 execution 可以精确
    @Pointcut("execution(* com.example.spring.service..*.*(..) )")
    public void pointCutexecution() {
    }

    ;

    // 只能精确到类的所有方法
    @Pointcut("within(com.prd.service.*)")
    public void pointCutWithin() {
    }

    ;

    // 只与方法参数有关
    @Pointcut("args(java.lang.String)")
    public void pointpointCutArgs() {
    }

    ;

    // 只要添加注解的才会被切面
    @Pointcut("@annotation(com.prd.anno.Luban)")
    public void pointAnno() {
    }

    ;

    // 这里的this只能写接口，而非具体实现类，因为proxyTargetClass = false，动态代理生成的对象是Proxy
    @Pointcut("this(com.prd.service.ISpringCoreDemoService)")
    public void pointThis() {
    }

    ;

    // 使用target时，就可以使用具体的实现类，即使proxyTargetClass = false
    @Pointcut("target(com.prd.service.SpringCoreDemoService)")
    public void pointTarget() {
    }

    ;

    // 声明通知  （位置 和 逻辑）位置就是指pointCut的位置
//    @Before("pointCutWithin()&&!pointpointCutArgs()")
    @Before("pointTarget()")
    public void before() {
        System.out.println("before");
    }
}
