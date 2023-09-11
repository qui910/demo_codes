


# Spring注解

## @Async
- [Spring中异步注解@Async的使用、原理及使用时可能导致的问题](https://daimingzhi.blog.csdn.net/article/details/107500036)



# 4 消息队列MQ

## 4.1 基于LinkedList手写一个消息队列
### 4.1.1 设计思路
首先我们想要设计一个方案的时候，要先捋清楚思路，想一下现有的，别人已经实现的方案，然后思考自己如何才能实现。(比如rabbitMq)
- 队列管理中心：集中管理所有创建的队列
- 提供方：往消息队列中发送消息
- 消费方：监听消息队列中的消息，并进行消费（如果监听到队列中新放入了消息，则自动消费处理）

![image.png](https://img-blog.csdnimg.cn/2021012209192754.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5OTE0ODk5,size_16,color_FFFFFF,t_70)
### 4.1.2 源码
`com.example.springbootdemo.queue.demo1`
通过线程通信方式，来通知消费者。

请注意：
-  LocalConsumer的注入问题，因为是异步方法且泛型子类，需要在@EnableAsync中加入proxyTargetClass=true
-  LocalConsumer的异步方法如果Spring启动后立刻执行且不占用主线程main，有几种方法：
    1. 实现CommandLineRunner接口，并重写run方法，但是此run方法还是main线程，并且不建议其中使用wait，会导致一些并发问题。
    2. @PostConstruct标注init方法，并在其中调用@Async异步方法，但是@PostConstruct标注的方法和@Async标注的方法不能是一个类，否则异步失效。还是在main线程中。
