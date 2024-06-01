[toc]
>介绍下学习Jav按设计模式的步骤

软件开发技术也包括一些招式和内功：Java、C#、C++等编程语言，Eclipse、Visual Studio等开发工具，JSP、ASP.net等开发技术，Struts、Hibernate、JBPM等框架技术，所有这些我们都可以认为是招式；而数据结构、算法、设计模式、重构、软件工程等则为内功。招式可以很快学会，但是内功的修炼需要更长的时间。

# 1 常用设计模式
|类型|模式名称|
|:---|:---|
|创建型模式 Creational Pattern|单例模式 Singleton Pattern|
|创建型模式 Creational Pattern|简单工厂模式 Simple Factory Pattern|
|创建型模式 Creational Pattern|工厂方法模式 Factory Method Pattern|
|创建型模式 Creational Pattern|抽象工厂模式 Abstract Factory Pattern|
|创建型模式 Creational Pattern|原型模式 Prototype Pattern|
|创建型模式 Creational Pattern|建造者模式 Builder Pattern|
|结构型模式 Structural Pattern|适配器模式 Adapter Pattern|
|结构型模式 Structural Pattern|桥接模式 Bridge Pattern|
|结构型模式 Structural Pattern|组合模式 Composite Pattern|
|结构型模式 Structural Pattern|装饰模式 Decorator Pattern|
|结构型模式 Structural Pattern|外观模式 Façade Pattern|
|结构型模式 Structural Pattern|享元模式 Flyweight Pattern|
|结构型模式 Structural Pattern|代理模式 Proxy Pattern|
|行为型模式 Behavioral Pattern|职责链模式 Chain of Responsibility Pattern|
|行为型模式 Behavioral Pattern|命令模式 Command Pattern|
|行为型模式 Behavioral Pattern|解释器模式 Interpreter Pattern|
|行为型模式 Behavioral Pattern|迭代器模式 Iterator Pattern|
|行为型模式 Behavioral Pattern|中介者模式 Mediator Pattern|
|行为型模式 Behavioral Pattern|备忘录模式 Memento Pattern|
|行为型模式 Behavioral Pattern|观察者模式 Observer Pattern|
|行为型模式 Behavioral Pattern|状态模式 State Pattern|
|行为型模式 Behavioral Pattern|策略模式 Strategy Pattern|
|行为型模式 Behavioral Pattern|模板方法模式 Template Method Pattern|
|行为型模式 Behavioral Pattern|访问者模式 Visitor Pattern|

# 2 面向对象设计原则
对于面向对象软件系统的设计而言，在支持可维护性的同时，提高系统的可复用性是一个至关重要的问题，如何同时提高一个软件系统的可维护性和可复用性是面向对象设计需要解决的核心问题之一。在面向对象设计中，可维护性的复用是以设计原则为基础的。每一个原则都蕴含一些面向对象设计的思想，可以从不同的角度提升一个软件结构的设计水平。

面向对象设计原则为支持可维护性复用而诞生，这些原则蕴含在很多设计模式中，它们是从许多设计方案中总结出的指导性原则。面向对象设计原则也是我们用于评价一个设计模式的使用效果的重要指标之一，在设计模式的学习中，大家经常会看到诸如“XXX模式符合XXX原则”、“XXX模式违反了XXX原则”这样的语句。

最常见的7种面向对象设计原则如下表所示

|设计原则名称|定义|
|:---|:---|
|单一职责原则 (Single Responsibility Principle, SRP)|一个类只负责一个功能领域中的相应职责|
|开闭原则 (Open-Closed Principle, OCP)|软件实体应对扩展开放，而对修改关闭|
|里氏代换原则 (Liskov Substitution Principle, LSP)|所有引用基类对象的地方能够透明地使用其子类的对象|
|依赖倒转原则 (Dependence Inversion Principle, DIP)|抽象不应该依赖于细节，细节应该依赖于抽象|
|接口隔离原则 (Interface Segregation Principle, ISP)|使用多个专门的接口，而不使用单一的总接口|
|合成复用原则 (Composite Reuse Principle, CRP)|尽量使用对象组合，而不是继承来达到复用的目的|
|迪米特法则 (Law of Demeter, LoD)|一个软件实体应当尽可能少地与其他实体发生相互作用|

## 2.1 单一职责原则 SRP
单一职责原则是最简单的面向对象设计原则，它用于控制类的粒度大小。单一职责原则定义如下： 单一职责原则(Single Responsibility Principle, SRP)：一个类只负责一个功能领域中的相应职责，或者可以定义为：就一个类而言，应该只有一个引起它变化的原因。

单一职责原则告诉我们：一个类不能太“累”！在软件系统中，一个类（大到模块，小到方法）承担的职责越多，它被复用的可能性就越小，而且一个类承担的职责过多，就相当于将这些职责耦合在一起，当其中一个职责变化时，可能会影响其他职责的运作，因此要将这些职责进行分离，将不同的职责封装在不同的类中，即将不同的变化原因封装在不同的类中，如果多个职责总是同时发生改变则可将它们封装在同一类中。

单一职责原则是实现高内聚、低耦合的指导方针，它是最简单但又最难运用的原则，需要设计人员发现类的不同职责并将其分离，而发现类的多重职责需要设计人员具有较强的分析设计能力和相关实践经验。

### 2.1.1 如何判断类的职责是否单一
eg1: 如果一个类，既要处理和其他微服务的交互，又要到数据库query，那么这就是两个职责，我们就应该将这个类分割开，划分到两个不同的类当中去

eg2:
```java
public class UserInfo {
  private long userId;
  private String username;
  private String email;
  private String telephone;
  private long createTime;
  private long lastLoginTime;
  private String avatarUrl;
  private String provinceOfAddress; // 省
  private String cityOfAddress; // 市
  private String regionOfAddress; // 区 
  private String detailedAddress; // 详细地址
  // ...省略其他属性和方法...
}
```
对于上述UserInfo类来说，里面全是User的一些属性，但是其中有将近半数是关于地址的，我们有理由将其细分为UserAddress类以及UserInfo类，但是在实际应用场景当中，我们需要考虑我们到底是准备如何使用这些数据的。

如果应用场景就是拿出用户相关的信息，那放在一起无伤大雅，但是如果是要做物流，电商的场景，那么我们单独想拿出地址相关信息的场景就会比较多了，这种情况最好就分成两个不同的类了。

实际开发当中，实际上一般情况下都是先写一个粗粒度的类，以满足业务的需求，随着业务的发展，如果粗粒度的类越来越庞大，我们就可以将这个粗粒度的类拆分成几个更细粒度的类，即–持续重构。

### 2.1.2 判断是否满足单一职责原则的判断准则
- 类中代码的行数，函数或者属性过多，会影响代码的可读性和可维护性，需要考虑对类进行拆分了
- 类依赖的其他类过多，或者依赖类的其他类过多，不符合高内聚，低耦合的设计思想，我们需要对其考虑进行拆分。
- 私有方法过多，我们需要考虑是否应该将私有方法独立到新的类当中，设置为public方法，供更多的类使用，从而提高代码的复用性
- 如果对于一个类，比较难取名字，只能用相对泛泛的名字，那很可能意味着这个类的职责有点过多了
- 类中的大量方法都是集中操作类中的某几个属性，那么就可以考虑将这几个属性和对应的方法拆分出来

### 2.1.3 示例
`com.ouyeel56.center.resource.service.nms.NmsAreaService`中既有查询，也有新增、删除、修改等。建议分为QueryService，和ManagerService
从而进行功能上线的区分，以便后续重构扩展。而且Query和Manager可以对应不同的Mapper，从功能上单一化类实现接口，实现接口的复用。

`com.ouyeel56.center.resource.service.nms.NmsStdAddressService`依赖Mapper过多，需要拆分

## 2.2 开闭原则 OCP
开闭原则是面向对象的可复用设计的**第一块基石**，它是最重要的面向对象设计原则。其定义如下：`开闭原则(Open-Closed Principle, OCP
)：一个软件实体应当对扩展开放，对修改关闭。即软件实体应尽量在不修改原有代码的情况下进行扩展`。

在开闭原则的定义中，软件实体可以指一个软件模块、一个由多个类组成的局部结构或一个独立的类。

任何软件都需要面临一个很重要的问题，即它们的需求会随时间的推移而发生变化。当软件系统需要面对新的需求时，我们应该尽量保证系统的设计框架是稳定的。如果一个软件设计符合开闭原则，那么可以非常方便地对系统进行扩展，而且在扩展时无须修改现有代码，使得软件系统在拥有适应性和灵活性的同时具备较好的稳定性和延续性。随着软件规模越来越大，软件寿命越来越长，软件维护成本越来越高，设计满足开闭原则的软件系统也变得越来越重要。

为了满足开闭原则，需要对系统进行抽象化设计，抽象化是开闭原则的关键。在`Java`、`C#`等编程语言中，可以为系统定义一个相对稳定的抽象层，而将不同的实现行为移至具体的实现层中完成。在很多面向对象编程语言中都提供了接口、抽象类等机制，可以通过它们定义系统的抽象层，再通过具体类来进行扩展。如果需要修改系统的行为，无须对抽象层进行任何改动，只需要增加新的具体类来实现新的业务功能即可，实现在不修改已有代码的基础上扩展系统的功能，达到开闭原则的要求。

总结：添加一个新的功能应该是在已有代码基础上扩展代码，而非修改已有的代码。

### 2.2.1 网络示例
代码实现的功能就是当TPS超过某个预设的最大值的时候，或者是错误的数量超过允许的最大值，那么就会触发警报

- 旧代码
```java
public class Alert {
    private AlertRule rule;
    private Notification notification;

    public Alert(AlertRule rule, Notification notification) {
        this.rule = rule;
        this.notification = notification;
    }
    
    public void check(String api, long requestCount, long errorCount, long durationOfSeconds) {
        long tps = requestCount / durationOfSeconds;
        if (tps > rule.getMatchedRule(api).getMaxTps()) {
          notification.notify(NotificationEmergencyLevel.URGENCY, "...");
        }
        if (errorCount > rule.getMatchedRule(api).getMaxErrorCount()) {
          notification.notify(NotificationEmergencyLevel.SEVERE, "...");
        }
    }
}
```
现在需要添加一个功能，当每秒钟请求数量超过某个阈值的时候，我们也要触发警告，发送通知。如果直接在上述代码中进行修改的话，主要是需要在check函数当中，添加一个新的输入参数，timeoutCount，表示超时的请求数量，然后再check函数里面加上对应的逻辑

- 旧代码业务变更
```java
public class Alert {
  // ...省略AlertRule/Notification属性和构造函数...

  // 改动一：添加参数timeoutCount
  public void check(String api, long requestCount, long errorCount, long timeoutCount, long durationOfSeconds) {
    long tps = requestCount / durationOfSeconds;
    if (tps > rule.getMatchedRule(api).getMaxTps()) {
      notification.notify(NotificationEmergencyLevel.URGENCY, "...");
    }
    if (errorCount > rule.getMatchedRule(api).getMaxErrorCount()) {
      notification.notify(NotificationEmergencyLevel.SEVERE, "...");
    }
    // 改动二：添加接口超时处理逻辑
    long timeoutTps = timeoutCount / durationOfSeconds;
    if (timeoutTps > rule.getMatchedRule(api).getMaxTimeoutTps()) {
      notification.notify(NotificationEmergencyLevel.URGENCY, "...");
    }
  }
}
```
这样的改动是有不少问题的，比如我们对于传入参数做了改动，那所有调用这个函数的地方都需要进行修改，这就是个不小的问题了；而且在修改了check函数以后，所有的单元测试都需要进行修改，会很麻烦的。

为了提高这整个类的拓展性，我们可以做以下的重构操作：
1. 将check函数的多个入参封装成ApiStatInfo类
2. 引入handler的概念，将if判断逻辑分散到各个handler当中

- 重构后代码结构
```java
public class Alert {
  private List<AlertHandler> alertHandlers = new ArrayList<>();

  public void addAlertHandler(AlertHandler alertHandler) {
    this.alertHandlers.add(alertHandler);
  }

  public void check(ApiStatInfo apiStatInfo) {
    for (AlertHandler handler : alertHandlers) {
      handler.check(apiStatInfo);
    }
  }
}

public class ApiStatInfo {//省略constructor/getter/setter方法
  private String api;
  private long requestCount;
  private long errorCount;
  private long durationOfSeconds;
}

public abstract class AlertHandler {
  protected AlertRule rule;
  protected Notification notification;
  public AlertHandler(AlertRule rule, Notification notification) {
    this.rule = rule;
    this.notification = notification;
  }
  public abstract void check(ApiStatInfo apiStatInfo);
}

public class TpsAlertHandler extends AlertHandler {
  public TpsAlertHandler(AlertRule rule, Notification notification) {
    super(rule, notification);
  }

  @Override
  public void check(ApiStatInfo apiStatInfo) {
    long tps = apiStatInfo.getRequestCount()/ apiStatInfo.getDurationOfSeconds();
    if (tps > rule.getMatchedRule(apiStatInfo.getApi()).getMaxTps()) {
      notification.notify(NotificationEmergencyLevel.URGENCY, "...");
    }
  }
}

public class ErrorAlertHandler extends AlertHandler {
  public ErrorAlertHandler(AlertRule rule, Notification notification){
    super(rule, notification);
  }

  @Override
  public void check(ApiStatInfo apiStatInfo) {
    if (apiStatInfo.getErrorCount() > rule.getMatchedRule(apiStatInfo.getApi()).getMaxErrorCount()) {
      notification.notify(NotificationEmergencyLevel.SEVERE, "...");
    }
  }
}

// 使用时
public class ApplicationContext {
  private AlertRule alertRule;
  private Notification notification;
  private Alert alert;

  public void initializeBeans() {
    alertRule = new AlertRule(/*.省略参数.*/); //省略一些初始化代码
    notification = new Notification(/*.省略参数.*/); //省略一些初始化代码
    alert = new Alert();
    alert.addAlertHandler(new TpsAlertHandler(alertRule, notification));
    alert.addAlertHandler(new ErrorAlertHandler(alertRule, notification));
  }
  public Alert getAlert() { return alert; }

  // 饿汉式单例
  private static final ApplicationContext instance = new ApplicationContext();
  private ApplicationContext() {
    instance.initializeBeans();
  }
  public static ApplicationContext getInstance() {
    return instance;
  }
}

public class Demo {
  public static void main(String[] args) {
    ApiStatInfo apiStatInfo = new ApiStatInfo();
    // ...省略设置apiStatInfo数据值的代码
    ApplicationContext.getInstance().getAlert().check(apiStatInfo);
  }
}
```
- 新代码业务变更
```java
public class Alert { // 代码未改动... }
public class ApiStatInfo {//省略constructor/getter/setter方法
  private String api;
  private long requestCount;
  private long errorCount;
  private long durationOfSeconds;
  private long timeoutCount; // 改动一：添加新字段
}
public abstract class AlertHandler { //代码未改动... }
public class TpsAlertHandler extends AlertHandler {//代码未改动...}
public class ErrorAlertHandler extends AlertHandler {//代码未改动...}
// 改动二：添加新的handler
public class TimeoutAlertHandler extends AlertHandler {//省略代码...}

public class ApplicationContext {
  private AlertRule alertRule;
  private Notification notification;
  private Alert alert;

  public void initializeBeans() {
    alertRule = new AlertRule(/*.省略参数.*/); //省略一些初始化代码
    notification = new Notification(/*.省略参数.*/); //省略一些初始化代码
    alert = new Alert();
    alert.addAlertHandler(new TpsAlertHandler(alertRule, notification));
    alert.addAlertHandler(new ErrorAlertHandler(alertRule, notification));
    // 改动三：注册handler
    alert.addAlertHandler(new TimeoutAlertHandler(alertRule, notification));
  }
  //...省略其他未改动代码...
}

public class Demo {
  public static void main(String[] args) {
    ApiStatInfo apiStatInfo = new ApiStatInfo();
    // ...省略apiStatInfo的set字段代码
    apiStatInfo.setTimeoutCount(289); // 改动四：设置tiemoutCount值
    ApplicationContext.getInstance().getAlert().check(apiStatInfo);
  }
}
```

### 2.2.3 一些思考
- 写代码的时候就需要想想有可能会有哪些需求上的变更，如何设计代码的结构，留好扩展点。

- 在识别出可变部分之后，要将可变部分封装起来，隔离变化，提供抽象化的不可变接口，给上层系统来使用。当具体的实现发生变化的时候，我们只需要基于相同的抽象接口，扩展一个新的实现，这样子上游的代码就几乎不需要修改了

- 基于接口而非实现的编程， 对扩展开放，对修改关闭

### 2.2.4 示例
`com.ouyeel56.center.resource.service.nms.NmsStdAddressService#saveNmsStdNodesAddress`存在多个if
情况的业务保存，当添加另外的保存业务时，需要修改代码才行

后续使用设计模型优化

## 2.3 里氏代换原则 LSP
里氏代换原则(Liskov Substitution Principle, LSP)：所有引用基类（父类）的地方必须能透明地使用其子类的对象，并且保证原来程序的逻辑性为不变以及正确性不被破坏。

里氏代换原则告诉我们，在软件中将一个基类对象替换成它的子类对象，程序将不会产生任何错误和异常，反过来则不成立，如果一个软件实体使用的是一个子类对象的话，那么它不一定能够使用基类对象。

例如有两个类，一个类为BaseClass，另一个是SubClass类，并且SubClass类是BaseClass类的子类，那么一个方法如果可以接受一个BaseClass类型的基类对象base的话，如：method1(base)，那么它必然可以接受一个BaseClass类型的子类对象sub，method1(sub)能够正常运行。反过来的代换不成立，如一个方法method2接受BaseClass类型的子类对象sub为参数：method2(sub)，那么一般而言不可以有method2(base)，除非是重载方法。

里氏代换原则是实现开闭原则的重要方式之一，由于使用基类对象的地方都可以使用子类对象，因此在程序中尽量使用基类类型来对对象进行定义，而在运行时再确定其子类类型，用子类对象来替换父类对象。

在使用里氏代换原则时需要注意如下几个问题：
- 子类的所有方法必须在父类中声明，或子类必须实现父类中声明的所有方法。根据里氏代换原则，为了保证系统的扩展性，在程序中通常使用父类来进行定义，如果一个方法只存在子类中，在父类中不提供相应的声明，则无法在以父类定义的对象中使用该方法。

- 在运用里氏代换原则时，尽量把父类设计为抽象类或者接口，让子类继承父类或实现父接口，并实现在父类中声明的方法，运行时，子类实例替换父类实例，可以很方便地扩展系统的功能，同时无须修改原有子类的代码，增加新的功能可以通过增加一个新的子类来实现。里氏代换原则是开闭原则的具体实现手段之一。

- Java语言中，在编译阶段，Java编译器会检查一个程序是否符合里氏代换原则，这是一个与实现无关的、纯语法意义上的检查，但Java编译器的检查是有局限的。

### 2.3.1 网络示例
```java
public class Transporter {
  private HttpClient httpClient;

  public Transporter(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public Response sendRequest(Request request) {
    // ...use httpClient to send request
  }
}

public class SecurityTransporter extends Transporter {
  private String appId;
  private String appToken;

  public SecurityTransporter(HttpClient httpClient, String appId, String appToken) {
    super(httpClient);
    this.appId = appId;
    this.appToken = appToken;
  }

  @Override
  public Response sendRequest(Request request) {
    if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(appToken)) {
      request.addPayload("app-id", appId);
      request.addPayload("app-token", appToken);
    }
    return super.sendRequest(request);
  }
}

public class Demo {    
  public void demoFunction(Transporter transporter) {    
    Reuqest request = new Request();
    //...省略设置request中数据值的代码...
    Response response = transporter.sendRequest(request);
    //...省略其他逻辑...
  }
}

// 里式替换原则
Demo demo = new Demo();
demo.demofunction(new SecurityTransporter(/*省略参数*/););
```

### 2.3.2 里氏替代原则 – 按照协议进行设计
子类在设计的时候，应当遵守父类的行为约定。父类定义了函数的行为约定，那么子类可以改变函数的内部实现逻辑，但不能改变函数原有的行为约定。

这里的行为约定指的是函数声明的要实现的功能；对于输入输出以及异常的约定

定义当中父类和子类之间的关系，也是可以替换成接口和实现类之间的关系的。


### 2.4 依赖倒转原则 DIP
如果说开闭原则是面向对象设计的目标的话，那么依赖倒转原则就是面向对象设计的主要实现机制之一，它是系统抽象化的具体实现。依赖倒转原则定义如下： 依赖倒转原则(Dependency Inversion Principle, DIP)：抽象不应该依赖于细节，细节应当依赖于抽象。换言之，要针对接口编程，而不是针对实现编程。

依赖倒转原则要求我们在程序代码中传递参数时或在关联关系中，尽量引用层次高的抽象层类，即使用接口和抽象类进行变量类型声明、参数类型声明、方法返回类型声明，以及数据类型的转换等，而不要用具体类来做这些事情。为了确保该原则的应用，一个具体类应当只实现接口或抽象类中声明过的方法，而不要给出多余的方法，否则将无法调用到在子类中增加的新方法。

在引入抽象层后，系统将具有很好的灵活性，在程序中尽量使用抽象层进行编程，而将具体类写在配置文件中，这样一来，如果系统行为发生变化，只需要对抽象层进行扩展，并修改配置文件，而无须修改原有系统的源代码，在不修改的情况下来扩展系统的功能，满足开闭原则的要求。

在实现依赖倒转原则时，我们需要针对抽象层编程，而将具体类的对象通过依赖注入(DependencyInjection, DI)的方式注入到其他对象中，依赖注入是指当一个对象要与其他对象发生依赖关系时，通过抽象来注入所依赖的对象。常用的注入方式有三种，分别是：构造注入，设值注入（Setter注入）和接口注入。构造注入是指通过构造函数来传入具体类的对象，设值注入是指通过Setter方法来传入具体类的对象，而接口注入是指通过在接口中声明的业务方法来传入具体类的对象。这些方法在定义时使用的是抽象类型，在运行时再传入具体类型的对象，由子类对象来覆盖父类对象。

## 2.5 接口隔离原则 ISP
接口隔离原则(Interface Segregation Principle, ISP)：使用多个专门的接口，而不使用单一的总接口，即客户端不应该依赖那些它不需要的接口。

根据接口隔离原则，当一个接口太大时，我们需要将它分割成一些更细小的接口，使用该接口的客户端仅需知道与之相关的方法即可。每一个接口应该承担一种相对独立的角色，不干不该干的事，该干的事都要干。这里的“接口”往往有两种不同的含义：一种是指一个类型所具有的方法特征的集合，仅仅是一种逻辑上的抽象；另外一种是指某种语言具体的“接口”定义，有严格的定义和结构，比如Java语言中的interface。对于这两种不同的含义，ISP的表达方式以及含义都有所不同：

1. 当把“接口”理解成一个类型所提供的所有方法特征的集合的时候，这就是一种逻辑上的概念，接口的划分将直接带来类型的划分。可以把接口理解成角色，一个接口只能代表一个角色，每个角色都有它特定的一个接口，此时，这个原则可以叫做“角色隔离原则”。

2. 如果把“接口”理解成狭义的特定语言的接口，那么ISP
表达的意思是指接口仅仅提供客户端需要的行为，客户端不需要的行为则隐藏起来，应当为客户端提供尽可能小的单独的接口，而不要提供大的总接口。在面向对象编程语言中，实现一个接口就需要实现该接口中定义的所有方法，因此大的总接口使用起来不一定很方便，为了使接口的职责单一，需要将大接口中的方法根据其职责不同分别放在不同的小接口中，以确保每个接口使用起来都较为方便，并都承担某一单一角色。接口应该尽量细化，同时接口中的方法应该尽量少，每个接口中只包含一个客户端（如子模块或业务逻辑类）所需的方法即可，这种机制也称为“定制服务”，即为不同的客户端提供宽窄不同的接口。

在使用接口隔离原则时，我们需要注意控制接口的粒度，接口不能太小，如果太小会导致系统中接口泛滥，不利于维护；接口也不能太大，太大的接口将违背接口隔离原则，灵活性较差，使用起来很不方便。一般而言，接口中仅包含为某一类用户定制的方法即可，不应该强迫客户依赖于那些它们不用的方法。

### 2.5.1 网络示例
#### 2.5.1.1 将接口视为一组API接口的集合
```java
public interface UserService {
  boolean register(String cellphone, String password);
  boolean login(String cellphone, String password);
  UserInfo getUserInfoById(long id);
  UserInfo getUserInfoByCellphone(String cellphone);
}

public class UserServiceImpl implements UserService {
  //...
}
```
当我们要实现删除操作的时候，最好不要直接在UserService里面加上这个方法，因为用户服务实质上不应该被默认直接具有删除的权限，相对应的，我们应该去创建一个新的接口，里面实现有删除相关的方法，实现接口的隔离。

```java
public interface UserService {
  boolean register(String cellphone, String password);
  boolean login(String cellphone, String password);
  UserInfo getUserInfoById(long id);
  UserInfo getUserInfoByCellphone(String cellphone);
}

public interface RestrictedUserService {
  boolean deleteUserByCellphone(String cellphone);
  boolean deleteUserById(long id);
}

public class UserServiceImpl implements UserService, RestrictedUserService {
  // ...省略实现代码...
}
```
#### 2.5.1.2 将接口理解为单个API接口或者函数
函数的设计需要功能单一，不要将多个不同的功能逻辑放在一个函数当中实现。
```java
public class Statistics {
  private Long max;
  private Long min;
  private Long average;
  private Long sum;
  private Long percentile99;
  private Long percentile999;
  //...省略constructor/getter/setter等方法...
}

public Statistics count(Collection<Long> dataSet) {
  Statistics statistics = new Statistics();
  //...省略计算逻辑...
  return statistics;
}
```
count方法算了太多不同的指标，应该将其分割开的。
```java
public Long max(Collection<Long> dataSet) { //... }
public Long min(Collection<Long> dataSet) { //... } 
public Long average(Colletion<Long> dataSet) { //... }
// ...省略其他统计函数...
```
#### 2.5.2.3 将接口理解为OOP中的接口的概念
即在设计接口的时候尽量减少大而全的接口的设计，每个接口都只做一件事情，这样子类在implement interface的时候，我们通过接口名也可以很清晰的知道在做什么，会有什么功能，也很大程度上提升了代码的复用性。

## 2.6 合成复用原则 CARP
合成复用原则就是在一个新的对象里通过关联关系（包括组合关系和聚合关系）来使用一些已有的对象，使之成为新对象的一部分；新对象通过委派调用已有对象的方法达到复用功能的目的。简言之：复用时要尽量使用组合/聚合关系（关联关系），少用继承。

在面向对象设计中，可以通过两种方法在不同的环境中复用已有的设计和实现，即通过组合/聚合关系或通过继承，但首先应该考虑使用组合/聚合，组合/聚合可以使系统更加灵活，降低类与类之间的耦合度，一个类的变化对其他类造成的影响相对较少；其次才考虑继承，在使用继承时，需要严格遵循里氏代换原则，有效使用继承会有助于对问题的理解，降低复杂度，而滥用继承反而会增加系统构建和维护的难度以及系统的复杂度，因此需要慎重使用继承复用。

通过继承来进行复用的主要问题在于继承复用会破坏系统的封装性，因为继承会将基类的实现细节暴露给子类，由于基类的内部细节通常对子类来说是可见的，所以这种复用又称“白箱”复用，如果基类发生改变，那么子类的实现也不得不发生改变；从基类继承而来的实现是静态的，不可能在运行时发生改变，没有足够的灵活性；而且继承只能在有限的环境中使用（如类没有声明为不能被继承）

## 2.7 迪米特法则 LKP
迪米特法则又称为最少知识原则(LeastKnowledge Principle, LKP)，其定义如下：
>迪米特法则(Law of Demeter, LoD)：一个软件实体应当尽可能少地与其他实体发生相互作用。即高内聚，低耦合

高内聚 低耦合是比较通用的设计思想，可以用来指导不同的粒度的代码的设计和开发的工作，比如系统，模块，类，甚至是函数。也可以去使用到不同的开发场景当中，比如微服务，框架，组件，类库等等。在这个原则当中，高内聚指的是类本身的设计，低耦合指的是类和类之间的依赖关系的设计。

如果一个系统符合迪米特法则，那么当其中某一个模块发生修改时，就会尽量少地影响其他模块，扩展会相对容易，这是对软件实体之间通信的限制，迪米特法则要求限制软件实体之间通信的宽度和深度。迪米特法则可降低系统的耦合度，使类与类之间保持松散的耦合关系。

迪米特法则还有几种定义形式，包括：不要和“陌生人”说话、只与你的直接朋友通信等，在迪米特法则中，对于一个对象，其朋友包括以下几类：
- (1) 当前对象本身(this)；
- (2) 以参数形式传入到当前对象方法中的对象；
- (3) 当前对象的成员对象；
- (4) 如果当前对象的成员对象是一个集合，那么集合中的元素也都是朋友；
- (5) 当前对象所创建的对象。

任何一个对象，如果满足上面的条件之一，就是当前对象的“朋友”，否则就是“陌生人”。在应用迪米特法则时，一个对象只能与直接朋友发生交互，不要与“陌生人”发生直接交互，这样做可以降低系统的耦合度，一个对象的改变不会给太多其他对象带来影响。

迪米特法则要求我们在设计系统时，应该尽量减少对象之间的交互，如果两个对象之间不必彼此直接通信，那么这两个对象就不应当发生任何直接的相互作用，如果其中的一个对象需要调用另一个对象的某一个方法的话，可以通过第三者转发这个调用。简言之，就是通过引入一个合理的第三者来降低现有对象之间的耦合度。

在将迪米特法则运用到系统设计中时，要注意下面的几点：在类的划分上，应当尽量创建松耦合的类，类之间的耦合度越低，就越有利于复用，一个处在松耦合中的类一旦被修改，不会对关联的类造成太大波及；在类的结构设计上，每一个类都应当尽量降低其成员变量和成员函数的访问权限；在类的设计上，只要有可能，一个类型应当设计成不变类；在对其他类的引用上，一个对象对其他对象的引用应当降到最低。

### 2.7.1 什么是高内聚？
指的是相近的功能应该放到同一个类当中，不相近的功能不要放在同一类。代码集中相对来说就会更加容易维护了。

### 2.7.2 什么是低耦合？
类和类之间的依赖关系简单清晰，即尽管两个类之间有依赖关系。一个类的代码的改动不会或者很少导致依赖类的代码的改动。

### 2.7.3 内聚和耦合的关系
![内聚耦合关系](https://i.loli.net/2020/03/23/yZVTqaQSgvbtE4l.png)

如图所示，左侧就是很好的高内聚低耦合的范例，我们将类最小化，即每个类只做一件事情，这样子其他依赖就会少很多。在修改或增加功能的时候，就不会对其他的类造成很大的影响。

### 2.7.4 网络示例
```java
public class NetworkTransporter {
    // 存在问题，NetworkTransporter作为一个底层类，不应该依赖于HtmlRequest类；与之相反的，因为其实他需要的是string address，以及byte的数组，那我们应该直接提供这些primitive type的数据
    public Byte[] send(HtmlRequest htmlRequest) {
      //...
    }
}

public class HtmlDownloader {
  private NetworkTransporter transporter;//通过构造函数或IOC注入

  public Html downloadHtml(String url) {
  // 根据上面NetworkTransporter我们希望做的改动，这里传入的不应该是HtmlRequest类的实例了
    Byte[] rawHtml = transporter.send(new HtmlRequest(url));
    return new Html(rawHtml);
  }
}

public class Document {
  private Html html;
  private String url;

  public Document(String url) {
    this.url = url;
    // downloader.downloadHtml逻辑复杂，不应该放在构造函数当中，也会很不好测试
    // 构造函数中使用new来做实例，违反了基于接口而非实现编程的原则
    HtmlDownloader downloader = new HtmlDownloader();
    this.html = downloader.downloadHtml(url);
  }
  //...
}
```
修改以后的代码：
```java
public class NetworkTransporter {
    // 省略属性和其他方法...
    public Byte[] send(String address, Byte[] data) {
      //...
    }
}


public class HtmlDownloader {
  private NetworkTransporter transporter;//通过构造函数或IOC注入

  // HtmlDownloader这里也要有相应的修改
  public Html downloadHtml(String url) {
    HtmlRequest htmlRequest = new HtmlRequest(url);
    Byte[] rawHtml = transporter.send(
      htmlRequest.getAddress(), htmlRequest.getContent().getBytes());
    return new Html(rawHtml);
  }
}


public class Document {
  private Html html;
  private String url;

  public Document(String url, Html html) {
    this.html = html;
    this.url = url;
  }
  //...
}

// 通过一个工厂方法来创建Document
public class DocumentFactory {
  private HtmlDownloader downloader;

  public DocumentFactory(HtmlDownloader downloader) {
    this.downloader = downloader;
  }

  public Document createDocument(String url) {
    Html html = downloader.downloadHtml(url);
    return new Document(url, html);
  }
}
```

# 参考
- [《设计模式》★★★★](https://www.bookstack.cn/read/design-pattern-java/README.md)
- [Leilei Chen博客(系列文档 ★★★)](https://llchen60.com/)

# 后续章节
- [创建型模式-简单工厂模式](/content/创建型模式-1-SimpleFactoryPattern.md)
- [创建型模式-工厂方法模式](/content/创建型模式-2-FactoryMethodPattern.md)
- [创建型模式-抽象工厂模式](/content/创建型模式-3-AbstractFactoryPattern.md)

