[toc]
>工厂方法模式

简单工厂模式虽然简单，但存在一个很严重的问题。当系统中需要引入新产品时，由于静态工厂方法通过所传入参数的不同来创建不同的产品，这必定要修改工厂类的源代码，将违背“开闭原则”，如何实现增加新产品而不影响已有代码？工厂方法模式应运而生，本文将介绍第二种工厂模式——工厂方法模式。

# 1 什么是工厂方法模式
定义一个用于创建对象的接口，让子类决定将哪一个类实例化。工厂方法模式让一个类的实例化延迟到其子类。工厂方法模式又简称为工厂模式(Factory Pattern)，又可称作虚拟构造器模式(Virtual Constructor Pattern)或多态工厂模式(Polymorphic Factory Pattern)。工厂方法模式是一种类创建型模式。

工厂方法模式通用类图如下：
![image.png](https://segmentfault.com/img/remote/1460000022276520)

## 1.1 通用框架实现
在工厂方法模式结构图中包含如下几个角色：
- Product（抽象产品）：它是定义产品的接口，是工厂方法模式所创建对象的超类型，也就是产品对象的公共父类。
- ConcreteProduct（具体产品）：它实现了抽象产品接口，某种类型的具体产品由专门的具体工厂创建，具体工厂和具体产品之间一一对应。
- Factory（抽象工厂）：在抽象工厂类中，声明了工厂方法(Factory Method)，用于返回一个产品。抽象工厂是工厂方法模式的核心，所有创建对象的工厂类都必须实现该接口。
- ConcreteFactory（具体工厂）：它是抽象工厂类的子类，实现了抽象工厂中定义的工厂方法，并可由客户端调用，返回一个具体产品类的实例。

与简单工厂模式相比，工厂方法模式最重要的区别是引入了抽象工厂角色，抽象工厂可以是接口，也可以是抽象类或者具体类

以下是通用框架的demo
- `org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1` 对通用框架的实现

- `org.example.designPatterns.creationalPattern.factoryMethodPattern.demo2` 以日志记录器为例
![image.png](https://static.sitestack.cn/projects/design-pattern-java/770c148856f7092ed5e42a1eff7f024d.png)
Logger接口充当抽象产品，其子类FileLogger和DatabaseLogger充当具体产品，LoggerFactory接口充当抽象工厂，其子类FileLoggerFactory和DatabaseLoggerFactory充当具体工厂。


# 4 工厂方法模式总结

## 4.1 优点
- 良好的封装性，代码结构清晰，调用者不用关系具体的实现过程，只需要提供对应的产品类名称即可。
- 易扩展性，在增加产品类的情况下，只需要适当的修改工厂类逻辑或者重新拓展一个工厂类即可。
- 屏蔽了产品类，产品类的变化调用者不用关心。比如在使用JDBC连接数据库时，只需要改动一个驱动的名称，数据库就会从Mysql切换到Oracle，极其灵活。

## 4.2 升级
在复杂的系统中，一个产品的初始化过程是及其复杂的，仅仅一个具体工厂实现可能有些吃力，此时最好的做法就是为每个产品实现一个工厂，达到一个工厂类只负责生产一个产品。

此时工厂方法模式的类图如下：
![image.png](https://segmentfault.com/img/remote/1460000022276521)

如上图，每个产品类都对应了一个工厂，一个工厂只负责生产一个产品，非常符合单一职责原则。

针对上述的升级过程，那么工厂方法中不需要传入抽象产品类了，因为一个工厂只负责一个产品的生产，此时的抽象工厂类如下：
```java
public abstract class AbstractFactory {  
    /**  
     * 工厂方法，需要子类实现  
     */  
    public abstract <T extends Product> T create();  
} 
```

# 5 Spring中的工厂方法模式
工厂方法模式在Spring底层被广泛的使用，最常用的例子就是AbstractFactoryBean。其实现了FactoryBean接口，这个接口中getObject()方法返回真正的Bean实例。

AbstractFactoryBean中的getObject()方法如下：
```java
public final T getObject() throws Exception{  
    //单例，从缓存中取，或者暴露一个早期实例解决循环引用  
    if (isSingleton()) {  
        return(this.initialized?this.singletonInstance:getEarlySingletonInstance());  
    }  
    //多实例  
    else{  
        //调用createInstance  
        returncreateInstance();  
     }  
}  
//创建对象  
protected abstract T createInstance() throws Exception;
```
从以上代码可以看出，创建对象的职责交给了createInstance这个抽象方法，由其子类去定制自己的创建逻辑。

下图显示了继承了AbstractFactoryBean的具体工厂类，如下：
![image.png](https://segmentfault.com/img/remote/1460000022276522)

其实与其说AbstractFactoryBean是抽象工厂类，不如说FactoryBean是真正的抽象工厂类，前者只是对后者的一种增强，完成大部分的可复用的逻辑。

比如常用的sqlSessionFactoryBean只是简单的实现了FactoryBean，并未继承AbstractFactoryBean，至于结论如何，具体看你从哪方面看了。

# 参考
- [~~Spring中的设计模式：工厂方法模式(已读)~~](https://segmentfault.com/a/1190000022276517)
- [~~Spring系列（七）：@FactoryBean注解用法介绍已读)~~](https://juejin.cn/post/7075556985579503653)