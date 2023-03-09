[toc]
>简单说明WebSocket的Java用法

# 1 WebSocket概述

## 1.1 WebSocket的发展历史
随着互联网的发展，传统的HTTP协议已经很难满足Web应用日益复杂的需求了。近年来，随着HTML5的诞生，WebSocket协议被提出，它实现了浏览器与服务器的**全双工通信**，扩展了浏览器与服务端的通信功能，使服务端也能主动向客户端发送数据。

我们知道，传统的HTTP协议是**无状态**的，每次请求（request）都要由客户端（如 浏览器）主动发起，服务端进行处理后返回response结果，而服务端很难主动向客户端发送数据；这种客户端是主动方，服务端是被动方的传统Web
模式。对于信息变化不频繁的Web应用来说造成的麻烦较小，而对于涉及实时信息的Web应用却带来了很大的不便，如带有即时通信、实时数据、订阅推送等功能的应 用。在WebSocket规范提出之前，开发人员若要实现这些实时性较强的功能，经常会使用折衷的解决方法：**轮询（polling）**和**Comet技术**。其实后者本质上也是一种轮询，只不过有所改进。

- 轮询是最原始的实现实时Web应用的解决方案。轮询技术要求客户端以设定的时间间隔周期性地向服务端发送请求，频繁地查询是否有新的数据改动。明显地，这种方法会导致过多不必要的请求，浪费流量和服务器资源。
>客户端以一定的时间间隔发送Ajax请求,优点实现起来比较简单、省事,不过缺点也很明显，请求有很大一部分是无用的，而且需要频繁建立和释放TCP连接，很消耗带宽和服务器资源。

- Comet技术又可以分为长轮询和流技术。
    - 长轮询改进了上述的轮询技术，减小了无用的请求。它会为某些数据设定过期时间，当数据过期后才会向服务端发送请求；这种机制适合数据的改动不是特别频繁的情况。
    >与普通轮询不同的地方在于，服务端接收到请求后会保持住不立即返回响应，等到有消息更新才返回响应并关闭连接，客户端处理完响应再重新发起请求。较之普通轮询没有无用的请求，但服务器保持连接也是有消耗的，如果服务端数据变化频繁的话和普通轮询并无两样。
    - 流技术通常是指客户端使用一个隐藏的窗口与服务端建立一个HTTP长连接，服务端会不断更新连接状态以保持HTTP长连接存活；这样的话，服务端就可以通过这条长连接主动将数据发送给客户端；流技术在大并发环境下，可能会考验到服务端的性能。
    >在页面中嵌入一个隐藏的iframe,将其src设为一个长连接的请求，这样服务端就能不断向客户端发送数据。优缺点与长轮询相仿。

这两种技术都是基于请求-应答模式，都不算是真正意义上的实时技术；它们的每一次请求、应答，都浪费了一定流量在相同的头部信息上，并且开发复杂度也较大。

伴随着HTML5推出的WebSocket，真正实现了Web的实时通信，使B/S模式具备了C/S模式的实时通信能力。

![image.png](https://img-blog.csdn.net/20180510223926952?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21vc2hvd2dhbWU
=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 1.2 WebSocket的工作流程
WebSocket的工作流程是这样的：浏览器通过JavaScript向服务端发出建立WebSocket连接的请求，在WebSocket连接建立成功后，客户端和服务端就可以通过TCP连接传输数据。因为WebSocket连接本质上是TCP连接，不需要每次传输都带上重复的头部数据，所以它的数据传输量比轮询和Comet技术小了很多。本文不详细地介绍WebSocket规范，主要介绍下WebSocket在Java Web中的实现。

## 1.3 WebSocket规范在Java中的实现
JavaEE 7中出了JSR-356:Java API for WebSocket规范。不少Web容器，如Tomcat,Nginx,Jetty等都支持WebSocket。Tomcat从7.0.27开始支持 WebSocket，从7.0.47开始支持JSR-356，下面的Demo代码也是需要部署在Tomcat7.0.47以上的版本才能运行。
　　
## 1.4 WebSocket开发
### 1.4.1 前端
WebSocket是HTML5开始提供的一种在单个TCP连接上进行全双工通讯的协议。在WebSocket API中，浏览器和服务器只需要做一个握手的动作，然后，浏览器和服务器之间就形成了一条快速通道。两者之间就直接可以数据互相传送。浏览器通过JavaScript向服务器发出建立WebSocket连接的请求，连接建立以后，客户端和服务器端就可以通过TCP连接直接交换数据。当你获取Web Socket连接后，你可以通过send()方法来向服务器发送数据，并通过onmessage事件来接收服务器返回的数据。

以下API用于创建 WebSocket 对象:
```javascript
var Socket = new WebSocket(url, [protocol] );
```
以上代码中的第一个参数 url, 指定连接的 URL。第二个参数 protocol 是可选的，指定了可接受的子协议。

### 1.4.2 后端

#### 常见Node.js实现WebSocket的方式
- [uWebSockets](https://github.com/uNetworking/uWebSockets)
- [Socket.IO](https://socket.io/)
- [WebSocket-Node](https://github.com/theturtle32/WebSocket-Node)

#### Java实现Websocket的方式
- Tomcat实现websocket方法，一般为创建WebSocketServer类方式。
- Spring整合websocket方法，即可以创建WebSocketServer类方式，也可以使用Spring特有的WebSocketHandler类。

总结来说，Java实现Websocket通常有两种方式：1、创建WebSocketServer类，里面包含open、close、message、error等方法；2、利用Springboot提供的WebSocketHandler类，创建其子类并重写方法。

# 2 创建WebSocketServer类方式

## 2.1 POM依赖
一般情况下，均使用SpringBoot配合开发WebSocket，这里首先引入SpringBoot WebSocket依赖
```xml
<!-- websocket支持包 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

## 2.2 创建配置类WebSocketConfig
在SpringBoot环境, 所以得先写个能扫描`@ServerEndpoint`的配置, 不然在客户端连接的时候会一直连不上。不是在SpringBoot下开发的可以跳过这一环节。

`ServerEndpointExporter`是由Spring官方提供的标准实现，用于扫描`ServerEndpointConfig`配置类和`@ServerEndpoint`注解实例。
- 如果使用内置的tomcat容器，那么必须用`@Bean`注入`ServerEndpointExporter`；
- 如果使用外置容器部署war包，那么不需要提供`ServerEndpointExporter`，扫描服务器的行为将交由外置容器处理，需要将注入`ServerEndpointExporter`的`@Bean`代码注解掉。

详见：`com.example.websocket.WebSocketConfigOne`

## 2.3 创建WebSocketServer
在WebSocket协议下，后端服务器相当于ws里面的服务端，需要用`@ServerEndpoint`指定访问路径，并使用`@Component`注入容器。

>`@ServerEndpoint`：当`ServerEndpointExporter`类通过Spring配置进行声明并被使用，它将会去扫描带有`@ServerEndpoint`注解的类。被注解的类将被注册成为一个WebSocket
端点。所有的配置项都在这个注解的属性中 ( 如:`@ServerEndpoint("/ws")` )

- `@ServerEndPoint`：必填属性：`Value`(URI映射)  ；扩展属性：`decoders`(解码器)、`encoders`(编码器)
- Session代表着服务端点与远程客户端点的一次会话。
- 容器会为每一个连接创建一个EndPoint的实例，需要利用实例变量来保存一些状态信息。`Session.getUserProperties`提供了一个可编辑的map来保存properties。

- Encoders and Decoders（编码器和解码器）:WebSocket Api 提供了encoders 和 decoders用于 Websocket Messages 与传统java 类型之间的转换
    - 编码器输入java对象，生成一种表现形式，能够被转换成Websocket message
    - 解码器执行相反的方法，它读入Websocket消息，然后输出java对象

```java
package com.zlxls.information;

import com.alibaba.fastjson.JSON;
import com.common.model.SocketMsg;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
/**
* 配置WebSocket解码器，用于发送请求的时候可以发送Object对象，实则是json数据
* sendObject()
*/
public class ServerEncoder implements Encoder.Text<SocketMsg> {
    @Override
    public void destroy() {
    }
 
    @Override
    public void init(EndpointConfig arg0) {
    }
 
    @Override
    public String encode(SocketMsg socketMsg) {
        try {
            return JSON.toJSONString(socketMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
```

```java
package com.zlxls.information;

import com.common.model.SocketMsg;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
/**
* 解码器执，它读入Websocket消息，然后输出java对象
*/
public class ServerDecoder implements Decoder.Text<SocketMsg>{
    @Override
    public void init(EndpointConfig ec){}
 
    @Override
    public void destroy(){}
 
    @Override
    public SocketMsg decode(String string) {
        // Read message...
        return new SocketMsg();
    }
 
    @Override
    public boolean willDecode(String string){
        // Determine if the message can be converted into either a
        // MessageA object or a MessageB object...
        return false;
    }
}
```

```bash
// WebSocketServer类上添加注解
@ServerEndpoint(value = "/myendpoint",encoders = {ServerEncoder.class},decoders = {ServerDecoder.class})
```

- 指定端点配置类。可以用来关联WebSocket和Http，方便从HttpSession中拿到用户登录信息等。
```java
package com.zlxls.information;
 
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
/**
 * 由于websocket的协议与Http协议是不同的，所以造成了无法直接拿到session。
 * 但是可以通过重写modifyHandshake，HandshakeRequest request可以获取httpSession
 */
public class GetHttpSessionConfigurator extends Configurator{
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
```

```bash
@ServerEndpoint(value = "/myendpoint",configurator=GetHttpSessionConfigurator.class)
```


- `session.getOpenSessions()` 从连接中获得Session对象。当被动的发送消息时，在诸如`@OnMessage`标注的方法中，可以通过此方式获取到所有的会话Session对象，并转发消息。例如：
```java
@ServerEndpoint("/send_all")
public class EchoAllEndpoint
{
    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            for (Session sess :session.getOpenSessions())
            {
                if (sess.isOpen()) {
                    sess.getBasicRemote().sendText(msg);
                }
            }
        } catch (IOExceptione e) { 
            log.error("发送消息异常",e);
        }
    }
}
```

- 在一个端点类中，至多可以为三个方法标注@OnMessage注解。消息类型分别为：text、binary、pong



## 2.4 Websocket调用
可以提供接口让前端调用，也可以由前端指定ws调用网址，直接使用onopen等方法。在业务代码中调用方法也是可以的。

### 2.4.1 提供接口进行消息推送
一个用户调用接口，主动将信息发给后端，后端接收后再主动推送给指定/全部用户

```java
import com.javatest.websocket.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {

    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("/websocket");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public Map<String,Object> pushToWeb(@PathVariable String cid, String message) {
        Map<String,Object> result = new HashMap<>();
        try {
            WebSocketServer.sendInfo(message,cid);
            result.put("status","success");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("status","fail");
            result.put("errMsg",e.getMessage());
        }
        return result;
    }
}
```

### 2.4.2 由前端指定ws调用网址直接使用
前端页面，注意是要使用ws协议

引入thymeleaf依赖，并配置yml文件
```xml
<!-- thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

页面代码详见：/resources/templates/websocket.html

此时是无法直接访问templates路径下的html文件，需要在yml中添加如下配置才可以：
```yml
# 静态文件请求匹配方式
spring.mvc.static-path-pattern=/**
# 修改默认的静态寻址资源目录
spring.resources.static-locations= classpath:/templates/,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
```
直接使用URL访问页面 http://127.0.0.1:8081/demo/websocket.html

参考：[Springboot中templates路径下的html文件为何无法访问？](https://blog.csdn.net/weixin_43987277/article/details/100641078)

或者创建Controller然后通过mvc指定页面访问
```java
/**
 * 注解 {@code @RestController} 如果使用@RestController注解，会默认将index识别为字符串解析到页面！
 * 我们需要的是index.html页面，并不是字符串
 */
@Controller
public class TemplateController {

    @RequestMapping("/websocket")
    public ModelAndView websocket(ModelAndView mv) {
        mv.setViewName("/websocket");
        mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;
    }
}
```
此时通过此URL访问：http://127.0.0.1:8081/demo/websocket

# 3 WebSocketHandler类方式
SpringBoot对WebSocket支持很友好，可以实现WebSocketHandler接口，或者继承TextWebSocketHandler等Spring已经实现WebSocketHandler接口的类。

WebSocketHandler 接口, 该接口提供了五个方法。
1. `afterConnectionEstablished()`: 建立新的socket连接后回调的方法。
2. `handleMessage()`: 接收客户端发送的Socket。
3. `handleTransportError()`: 连接出错时，回调的方法。
4. `afterConnectionClosed()`: 连接关闭时，回调的方法。
5. `supportsPartialMessages()`: 这个是WebSocketHandler是否处理部分消息，没什么用，返回false就可以了。

## 3.1 继承TextWebSocketHandler
代码详见：com.example.websockethandler.WebSocketTwoHandler

## 3.2 自定义拦截器
如果希望能够把WebSocketSession和HttpSession对应起来，这样就能根据当前不同的Session，定向对WebSocketSession进行数据返回。在Spring中有一个拦截器接口`HandshakeInterceptor`，可以实现这个接口，来拦截握手过程，向其中添加属性。

com.example.websockethandler.WebSocketTwoInterceptor

## 3.3 核心配置类
实现`WebSocketConfigurer`接口实现它提供的注册方法

## 3.4 页面
http://127.0.0.1:8081/demo/websockettwo

# 4 WebSocket和STOMP协议
WebSocket协议允许应用程序之间实现双向通信。重要的是要知道HTTP仅用于初始握手。初次握手之后，HTTP连接将升级为被WebSocket使用的新TCP/IP连接。

WebSocket协议是一种相当低级的协议。它定义了如何将字节流转换为帧。帧可以包含文本或二进制消息。由于消息本身不提供有关如何路由或处理它的任何其他信息，因此很难在不编写其他代码的情况下实现更复杂的应用程序。幸运的是，WebSocket规范允许在更高的应用程序级别上使用子协议。STOMP是其中之一，由Spring Framework支持。

## 4.1 STOMP 协议
STOMP是一种简单的基于文本的消息传递协议，最初是为Ruby，Python和Perl等脚本语言创建的，用于连接企业级消息代理。由于STOMP，使不同语言开发的客户端和代理可以相互发送和接收消息。WebSocket协议有时称为Web TCP。以此类推，STOMP被称为Web HTTP。它定义了一些映射到WebSocket帧的帧类型，例如CONNECT，SUBSCRIBE，UNSUBSCRIBE，ACK或SEND。一方面，这些命令非常便于管理通信，另一方面，它们允许我们实现具有更复杂功能的解决方案，如消息确认。

它是高级的流文本定向消息协议，是一种为 MOM (Message Oriented Middleware，面向消息的中间件) 设计的简单文本协议。

它提供了一个可互操作的连接格式，允许 STOMP 客户端与任意 STOMP 消息代理 (Broker) 进行交互，类似于 OpenWire (一种二进制协议)。

由于其设计简单，很容易开发客户端，因此在多种语言和多种平台上得到广泛应用。其中最流行的 STOMP 消息代理是 Apache ActiveMQ。

STOMP 协议使用一个基于 (frame) 的格式来定义消息，与 Http 的 request 和 response 类似 。

## 4.2 服务端:Spring Boot和WebSocket
为了构建WebSocket服务器端，我们将利用Spring Boot框架，该框架使得在Java中开发独立程序和Web应用程序更快。 Spring Boot包含spring-WebSocket模块，该模块与Java WebSocket API标准（JSR-356）兼容。

### 4.2.1 添加WebSocket库依赖项。
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>            
  <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

如果计划使用JSON格式传输消息，则可能还需要包含GSON或Jackson依赖项。您还可能还需要一个安全框架，例如Spring Security。

### 4.2.2 可以配置Spring启用WebSocket和STOMP消息传递。
```java
@Configuration
// @EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
// 开始支持@MessageMapping,就像是使用@requestMapping一样。
@EnableWebSocketMessageBroker
public class WebSocketConfigThree implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp的节点(endpoint),并指定使用SockJS协议。
        registry.addEndpoint("/marcopolo").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue","/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```
- 使用了`@EnableWebSocketMessageBroker`注解。这表明这个配置类不仅配置了WebSocket，还配置了基于代理的STOMP消息。
- 重载了`registerStompEndpoints()`方法，将“/marcopolo”注册为STOMP端点。这个路径与之前发送和接收消息的目的地路径有所不同。这是一个端点，客户端在订阅或发布消息到目的地路径前，要连接该端点。
- 重载了`configureMessageBroker()`方法，配置了一个简单的消息代理。这个方法是可选的，如果不重载它的话，将会自动配置一个简单的内存消息代理，用它来处理以“/topic”为前缀的消息。但是在本例中，我们重载了这个方法，所以消息代理将会处理前缀为“/topic”和“/queue”的消息。除此之外，发往应用程序的消息将会带有“/app”前缀。

![image.png](https://img-blog.csdnimg.cn/20190828212413394.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3ODcwNDIx,size_16,color_FFFFFF,t_70)

#### 4.2.2.1 WebSocket握手拦截器
自定义初始HTTP WebSocket握手请求的最简单方法是实现`HandshakeInterceptor`接口，它暴露握手方法的“之前”和“之后”。这样的拦截器可用于阻止握手或使任何属性可用于`WebSocketSession`。

例如，有一个内置拦截器，用于将HTTP会话属性传递给WebSocket会话。`HttpSessionHandshakeInterceptor`

- 自定义一个WebSocket握手拦截器
```java
/**
 * WebSocket握手拦截器
 *
 * 可做一些用户认证拦截处理
 * @author pang
 **/
public class MyHandshakeInterceptorThree implements HandshakeInterceptor {
    /**
     * WebSocket握手连接
     * @return 返回是否同意握手
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        // 通过url的query参数获取认证参数
        String token = req.getServletRequest().getParameter("token");
        // 根据token认证用户，不通过返回拒绝握手
        Principal user = authenticate(token);
        if(user == null){
            return false;
        }
        //保存认证用户
        attributes.put("user", user);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }

    /**
     * 根据token认证授权
     * @param token
     */
    private Principal authenticate(String token){
        // 实现用户的认证并返回用户信息，如果认证失败返回 null
        // 用户信息需继承Principal并实现getName()方法，返回全局唯一值
        return null;
    }
}
```

- 添加自定义的WebSocket握手拦截器
```java
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addInterceptors(new MyHandshakeInterceptorThree());
    }
```

#### 4.2.2.2 WebSocket握手处理器
更高级的选项是扩展`DefaultHandshakeHandler`执行WebSocket握手步骤，包括验证客户端来源，协商子协议等。如果应用程序需要配置自定义RequestUpgradeStrategy以适应WebSocket
服务器引擎和尚不支持的版本，则应用程序可能还需要使用此选项（有关此主题的更多信息，请参阅部署）。



### 4.2.3 处理来自客户端的STOMP消息
```java
/**
 * 控制器代码
 *
 * {@code WsController}注解，表示注册一个Controller，WebSocket的消息处理需要放在Controller下
 * @author pang
 **/
@RestController
public class WebSocketControllerThree {

    /**
     * Spring WebSocket消息发送模板
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送广播通知
     *  (1){@code @MessageMapping} 和 {@code @RequestMapping}功能类似，浏览器向服务器发起消息，映射到该地址。
     *  (2){@code @MessageMapping("/addNotice") }用于接收客户端发来的消息，客户端发送消息地址为：/app/addNotice，
     *  如果服务器接受到了消息，就会对订阅了{@code @SendTo}括号中的地址的浏览器发送消息。
     *  (3){@code @SendTo("/topic/notice")} 用于向客户端发送广播消息（方式一），客户端订阅消息地址为：/topic/notice
     */
    @MessageMapping("/addNotice")
    @SendTo("/topic/notice")
    public WsMessageThree say(String notice, Principal fromUser) throws Exception {
        // 业务处理
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName(fromUser.getName());
        msg.setContent(notice);

        //向客户端发送广播消息（方式二），客户端订阅消息地址为：/topic/notice
//        messagingTemplate.convertAndSend("/topic/notice", msg);
        return msg;
    }

    /**
     * 发送点对点消息
     * {@code @MessageMapping("/msg") } 指明接收客户端发来的消息，客户端发送消息地址为：/app/msg
     * {@code @SendToUser("/queue/msg/result")} 指明向当前发消息客户端（就是自己）发送消息的反馈结果，客户端订阅消息地址为：/user/queue/msg/result
     */
    @MessageMapping("/msg")
    @SendToUser("/queue/msg/result")
    public boolean sendMsg(WsMessageThree message, Principal fromUser){
        // 业务处理
        message.setFromName(fromUser.getName());

        // 向指定客户端发送消息，第一个参数Principal.name为前面websocket握手认证通过的用户name（全局唯一的），客户端订阅消息地址为：/user/queue/msg/new
        messagingTemplate.convertAndSendToUser(message.getToName(), "/queue/msg/new", message);
        return true;
    }
}
```
除了`@MessagingMapping`注解以外，Spring还提供了`@SubscribeMapping`注解。与`@MessagingMapping`注解方法类似，当收到STOMP订阅消息的时候，带有`@SubscribeMapping`注解的方法将会触发。

#### 4.2.3.1 发送消息到客户端
Spring提供了两种发送数据给客户端的方法：
- 使用`@SendTo`或`@SendToUser("/queue/msg/result")`发送广播或点对点消息，但是必须是作为客户端发送的一条消息的响应。
- 使用`SimpMessagingTemplate`（或者其接口`SimpMessageSendingOperations`）在应用的任何地方发送消息，甚至不必以首先接收一条消息作为前提。


#### 4.2.3.2 为目标用户发送消息 
可以使用SpringSecurity来认证用户，并为目标用户处理消息。在使用Spring和STOMP消息功能的时候，我们有三种方式利用认证用户：
- `@MessageMapping`和`@SubscribeMapping`标注的方法能够使用Principal来获取认证用户；
- `@MessageMapping`、`@SubscribeMapping`和`@MessageException`方法返回的值能够以消息的形式发送给认证用户；
- `SimpMessagingTemplate`能够发送消息给特定用户。

![image.png](https://img-blog.csdnimg.cn/20190828221117553.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3ODcwNDIx,size_16,color_FFFFFF,t_70)

#### 4.2.3.3 处理消息异常
在Spring MVC中，如果在请求处理中，出现异常的话，`@ExceptionHandler`方法将有机会处理异常。与之类似，我们也可以在某个控制器方法上添加`@MessageExceptionHandler`注解，让它来处理`@MessageMapping`方法所抛出的异常。

```java
    @MessageExceptionHandler(MessagingException.class)
    @SendToUser("/queue/errors")
    public String handleExceptions(Throwable t) {
        log.error("Error handing message: {}",t.getMessage());
        return t.getMessage();
    }
```

- `@MessageExceptionHandler`标注的方法能够处理消息方法中所抛出的异常。
- 也可以以参数的形式声明它所能处理的异常，或者以数组参数的形式指定多个异常类型。
```
@MessageExceptionHandler(MessagingException.class)
@MessageExceptionHandler({MessagingException.class,OtherException.class})
```
- 除了以日志的方式记录了所发生的错误，这个方法还可以回应一个错误。然后由客户端监听处理
```
@SendToUser("/queue/errors")
```

## 4.3 客户端
客户端连接WebSocket，以下分为两种情况：
- Java Client方式连接
- 前台JS通过SockJS方式连接

### 4.3.1 JS方式
- 本案例各个通信地址
通信地址除了握手请求地址最好写完整的地址，其它地址均不用写域名或IP
- 握手请求(connect)：http://域名或IP/websocket?token=认证token
- 广播订阅地址(subscribe)：/topic/notice
- 个人消息订阅地址(subscribe)：/user/queue/msg/new
- 发送广播通知(send)：/app/addNotice
- 发送点对点消息(send)：/app/msg
- 获取消息发送结果(subscribe)：/user/queue/msg/result

开启3个tab页，进行广播，点对点测试。

http://127.0.0.1:8081/demo/websocketthree

### 4.3.2 Java Client方式
```java
/**
 * Spring STOMP客户端
 * @author pang
 **/
@Service
public class WebSocketClientThree {
    @Autowired
    private WebSocketStompClient stompClient;

    private StompSession stompSession;

    public void openClient(String fromUser) throws ExecutionException, InterruptedException {
        StompSessionHandler sessionHandler = new MySessionHandler();
        stompSession = stompClient.connect("ws://127.0.0.1:8081/demo/clientws?token="+fromUser,
                sessionHandler).get();
    }

    public void sendTopic(String message) {
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName("user4");
        msg.setContent(message);
        stompSession.send("/topic/notice",msg);
    }

    public void sendToUser(String toUser,String message) {
        WsMessageThree msg = new WsMessageThree();
        msg.setFromName("user4");
        msg.setContent(message);
        msg.setToName(toUser);
        stompSession.send("/user/"+toUser+"/queue/msg/new",msg);
    }
}
```
这里不能使用/marcopolo，因为其有withSockJS
虽然这里连接的是/clientws，而页面连接的是/marcopolo 但是二者是可以通信的

这里client的启动连接和发送消息操作，均通过restful接口方式操作
```java
    @GetMapping("/sendClientMsg")
    public void sendClientMsg(@RequestParam("message") String message,@RequestParam("toUser") String toUser) throws ExecutionException,
            InterruptedException {
        if (StrUtil.isNotBlank(toUser)) {
            clientThree.sendToUser(toUser,message);
        } else {
            clientThree.sendTopic(message);
        }
    }


    @GetMapping("/openClient")
    public void openClient() throws ExecutionException, InterruptedException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String fromUser = request.getHeader("token");
        clientThree.openClient(fromUser);
    }
```

## 4.4 启用STOMP代理中继
Spring Boot允许您使用任何具有STOMP协议的完整消息系统（例如，ActiveMQ，RabbitMQ），并且外部代理可以支持更多STOMP操作（例如，确认，租借）而不是我们使用的简单代理。 STOMP Over WebSocket提供有关WebSocket和STOMP协议的信息。它列出了处理STOMP协议的消息传递系统，可能是在生产中使用的更好的解决方案。特别是由于请求数量很大，消息代理需要进行集群（Spring的简单消息代理不适合集群）。然后，不需要在WebSocketConfig中启用简单代理，而是需要启用Stomp代理中继，该中继将消息转发到外部消息代理和从外部消息代理转发消息。总而言之，外部消息代理可以帮助您构建更具伸缩性和可靠性的解决方案。



```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigThree implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/marcopolo").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用STOMP
        registry.enableStompBrokerRelay("/topic","/queue")
                .setRelayHost("rabbit.someotherserver")
                .setRelayPort(62623)
                .setClientLogin("marcopolo")
                .setClientPasscode("letmein01");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```
- `configureMessageBroker()`方法的第一行代码启用了STOMP代理中继（broker relay）功能，并将其目的地前缀设置为“/topic”和“/queue”。这样的话，Spring就能知道所有目的地前缀为“/topic”或“/queue”的消息都会发送到STOMP代理中。根据你所选择的STOMP代理不同，目的地的可选前缀也会有所限制。
- `enableStompBrokerRelay()`和`setApplicationDestinationPrefixes()`方法都接收可变长度的String参数，所以我们可以配置多个目的地和应用前缀。
- 默认情况下，STOMP代理中继会假设代理监听localhost的61613端口，并且客户端的username和password均为“guest”。如果你的STOMP代理位于其他的服务器上，或者配置成了不同的客户端凭证，那么我们可以在启用STOMP代理中继的时候，需要配置这些细节信息：
```java
.setRelayHost("rabbit.someotherserver")
.setRelayPort(62623)
.setClientLogin("marcopolo")
.setClientPasscode("letmein01");
```

![image.png](https://img-blog.csdnimg.cn/20190828213138354.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3ODcwNDIx,size_16,color_FFFFFF,t_70)



# 5 websocket 注解介绍

## 5.1 @MessageMapping
`@MessageMapping`，用户处理client发送过来的消息，被注解的方法可以具有以下参数。  
- `Message`：用于接收完整的消息
- `MessageHeaders`：用于接收消息中的头信息
- `MessageHeaderAccessor`/`SimpMessageHeaderAccessor`/`StompHeaderAccessor`：用于接收消息中的头信息，并且构建绑定Spring中的一些附加信息
- `@Headers`：用于接收消息中的所有header。这个参数必须用`java.util.Map`
- `@Header`：用于接收特定的头值
- `@Payload`：接受STOMP协议中的Body，可以用`@javax.validation`进行注释, Spring的`@Validated`自动验证 （类似`@RequestBody`）
- `DestinationVariable`: 用于提取header中destination模板变量 （类似`@PathVariable`）
- `java.security.Principal`：接收在WebSocket HTTP握手时登录的用户
    
当`@MessageMapping`方法返回一个值时，默认情况下，该值在被序列化为Payload后，作为消息发送到向订阅者广播的“brokerChannel”，且消息destination与接收destination相同，但前缀为变为配置的值。
    
可以使用`@SendTo`指定发送的destination，将Payload消息，进行广播发送到订阅的客户端。`@SendToUser`是会向与当条消息关联的用户发送回执消息，还可以使用`SimpMessagingTemplate`发送代替`@SendTo`/`@SendToUser`进行消息的发送
    
## 5.2 @SubscribeMapping 
`@SubscribeMapping`注释与`@MessageMapping`结合使用，以缩小到订阅消息的映射。在这种情况下，`@MessageMapping`注释指定目标，而`@SubscribeMapping`仅表示对订阅消息的兴趣。
    
`@SubscribeMapping`通常与`@MessageMapping`没有区别。关键区别在于，`@SubscribeMapping`的方法的返回值被序列化后，会发送到“clientOutboundChannel”，而不是“brokerChannel”，直接回复到客户端，而不是通过代理进行广播。这对于实现一次性的、请求-应答消息交换非常有用，并且从不占用订阅。这种模式的常见场景是当数据必须加载和呈现时应用程序初始化。
    
`@SendTo`注释`@SubscribeMapping`方法，在这种情况下，返回值被发送到带有显式指定目标目的地的“brokerChannel”。
    
## 5.3 @MessageExceptionHandler
应用程序可以使用`@MessageExceptionHandler`方法来处理`@MessageMapping`方法中的异常。`@MessageExceptionHandler`方法支持灵活的方法签名，并支持与`@MessageMapping`方法相同的方法参数类型和返回值。与Spring MVC中的`@ExceptionHandler`类似。

```java
@Controller
public abstract class BaseController {
    @MessageExceptionHandler(MyException.class)
    public xxx handleException(MyException exception) {
        // ...
        return Xxx;
    }
}
public class WsController extends BaseController {
    // ...
}
```

# 参考资料
- [SpringBoot2.0集成WebSocket，实现后台向前端推送信息](https://blog.csdn.net/moshowgame/article/details/80275084)
- [Websocket技术的Java实现(下篇)](https://blog.csdn.net/KeepStruggling/article/details/105923142)
- [Using Spring Boot for WebSocket Implementation with STOMP](https://www.toptal.com/java/stomp-spring-boot-websocket)
- [Spring Websocket 中文文档 (spring5)](https://cloud.tencent.com/developer/article/1532220)
- [官网Spring5 WebSocket文档](https://docs.spring.io/spring-framework/docs/5.0.7.RELEASE/spring-framework-reference/web-reactive.html#webflux-websocket)
- [Spring官方文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [Stomp官网](http://stomp.github.io/)
- ~~[Spring 与使用STOMP消息](https://blog.csdn.net/qq_27870421/article/details/100126900)~~
- [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)
- [Java StompSessionHandler类代码示例](https://vimsky.com/examples/detail/java-class-org.springframework.messaging.simp.stomp.StompSessionHandler.html)
- ~~[Spring5高级编程------Spring-WebSocket中STOMP配置的加载机制](https://blog.csdn.net/sanjun333/article/details/107078010)~~

系列文章
- ~~[springboot+websocket构建在线聊天室（群聊+单聊）](https://blog.csdn.net/qq_41603102/article/details/82492040s)~~
- ~~[SpringBoot+STOMP 实现聊天室（单聊+多聊）及群发消息详解](https://blog.csdn.net/qq_41603102/article/details/88351729)~~
- [Websocket Stomp+RabbitMQ实现消息推送](https://blog.csdn.net/qq_41603102/article/details/89383524)
