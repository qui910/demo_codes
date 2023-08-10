[toc]
>简单工厂模式

工厂模式是最常用的一类创建型设计模式，通常我们所说的工厂模式是指工厂方法模式，它也是使用频率最高的工厂模式。本章将要学习的简单工厂模式是工厂方法模式的“小弟”，它不属于GoF 23种设计模式，但在软件开发中应用也较为频繁，通常将它作为学习其他工厂模式的入门。此外，工厂方法模式还有一位“大哥”——抽象工厂模式。这三种工厂模式各具特色，难度也逐个加大，在软件开发中它们都得到了广泛的应用，成为面向对象软件中常用的创建对象的工具。

# 1 什么是简单工厂模式
简单工厂模式(Simple Factory Pattern)：定义一个工厂类，它可以根据参数的不同返回不同类的实例，被创建的实例通常都具有共同的父类。因为在简单工厂模式中用于创建实例的方法是静态(static)方法，因此简单工厂模式又被称为静态工厂方法(Static Factory Method)模式，它属于类创建型模式。

![简单工厂模式结构图](https://static.sitestack.cn/projects/design-pattern-java/d3d9172d0441fd4e9e9826a1294a3f62.png)

## 1.1 角色
在简单工厂模式结构图中包含如下几个角色：
- Factory（工厂角色）：工厂角色即工厂类，它是简单工厂模式的核心，负责实现创建所有产品实例的内部逻辑；工厂类可以被外界直接调用，创建所需的产品对象；在工厂类中提供了静态的工厂方法factoryMethod()，它的返回类型为抽象产品类型Product。
- Product（抽象产品角色）：它是工厂类所创建的所有对象的父类，封装了各种产品对象的公有方法，它的引入将提高系统的灵活性，使得在工厂类中只需定义一个通用的工厂方法，因为所有创建的具体产品对象都是其子类对象。
- ConcreteProduct（具体产品角色）：它是简单工厂模式的创建目标，所有被创建的对象都充当这个角色的某个具体类的实例。每一个具体产品角色都继承了抽象产品角色，需要实现在抽象产品中声明的抽象方法。

# 2 如果创建工厂模式
- 首先，需要对产品类进行重构，不能设计一个包罗万象的产品类，而需根据实际情况设计一个产品层次结构，将所有产品类公共的代码移至抽象产品类，并在抽象产品类中声明一些抽象方法，以供不同的具体产品类来实现。

- 在具体产品类中实现了抽象产品类中声明的抽象业务方法，不同的具体产品类可以提供不同的实现。

- 简单工厂模式的核心是工厂类，在没有工厂类之前，客户端一般会使用new关键字来直接创建产品对象，而在引入工厂类之后，客户端可以通过工厂类来创建产品，在简单工厂模式中，工厂类提供了一个静态工厂方法供客户端使用，根据所传入的参数不同可以创建不同的产品对象

# 3 示例

## 3.1 示例1
简单工厂模式示例
![image.png](https://static.sitestack.cn/projects/design-pattern-java/044f54f108c2e4a2b0ba669e48976a1e.png)

`org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo1`

## 3.2 示例2
![image.png](https://static.sitestack.cn/projects/design-pattern-java/7a49144341ab0662245f08003ec916af.png)

`org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo2`

## 3.3 示例3
可以将静态工厂方法的参数存储在XML或properties格式的配置文件中，由读取配置文件来决定初始化哪个实例

`org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo3`

# 4 简单工厂模式总结
简单工厂模式提供了专门的工厂类用于创建对象，将对象的创建和对象的使用分离开，它作为一种最简单的工厂模式在软件开发中得到了较为广泛的应用。

## 4.1 主要优点
简单工厂模式的主要优点如下：
1. 工厂类包含必要的判断逻辑，可以决定在什么时候创建哪一个产品类的实例，客户端可以免除直接创建产品对象的职责，而仅仅“消费”产品，简单工厂模式实现了对象创建和使用的分离。
2. 客户端无须知道所创建的具体产品类的类名，只需要知道具体产品类所对应的参数即可，对于一些复杂的类名，通过简单工厂模式可以在一定程度减少使用者的记忆量。
3. 通过引入配置文件，可以在不修改任何客户端代码的情况下更换和增加新的具体产品类，在一定程度上提高了系统的灵活性。

## 4.1 主要缺点
简单工厂模式的主要缺点如下：
1. 由于工厂类集中了所有产品的创建逻辑，职责过重，一旦不能正常工作，整个系统都要受到影响。
2. 使用简单工厂模式势必会增加系统中类的个数（引入了新的工厂类），增加了系统的复杂度和理解难度。
3. 系统扩展困难，一旦添加新产品就不得不修改工厂逻辑，在产品类型较多时，有可能造成工厂逻辑过于复杂，不利于系统的扩展和维护。
4. 简单工厂模式由于使用了静态工厂方法，造成工厂角色无法形成基于继承的等级结构。
  
## 4.3 适用场景
在以下情况下可以考虑使用简单工厂模式：
1. 工厂类负责创建的对象比较少，由于创建的对象较少，不会造成工厂方法中的业务逻辑太过复杂。
2. 客户端只知道传入工厂类的参数，对于如何创建对象并不关心。


# 5 Spring中的工厂模式

## 5.1 BeanFactory和FactoryBean
Spring中提起工厂模式首先想到的就是BeanFactory和FactoryBean。BeanFactory可以被视为一种工厂模式，用于创建和管理Bean实例。而FactoryBean则是一种更高级的工厂模式，它允许开发人员在创建Bean时进行更多的定制和控制。通过实现FactoryBean接口，开发人员可以创建更复杂的Bean实例，也可以在Bean的创建过程中加入自定义的逻辑。

Spring 中有两种类型的Bean，一种是普通Bean，另一种是工厂Bean 即 FactoryBean。FactoryBean跟普通Bean不同，其返回的对象不是指定类的一个实例，而是该FactoryBean的getObject方法所返回的对象。创建出来的对象是否属于单例由isSingleton中的返回决定。

一般情况下，Spring通过反射机制利用的class属性指定实现类实例化Bean，在某些情况下，实例化Bean过程比较复杂，如果按照传统的方式，则需要在中提供大量的配置信息。配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。Spring为此提供了一个org.springframework.bean.factory.FactoryBean的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑。FactoryBean接口对于Spring框架来说占用重要的地位，Spring自身就提供了70多个FactoryBean的实现。它们隐藏了实例化一些复杂Bean的细节，给上层应用带来了便利。从Spring3.0开始，FactoryBean开始支持泛型，即接口声明改为FactoryBean的形式

以Bean结尾，表示它是一个Bean，不同于普通Bean的是：它是实现了FactoryBean接口的Bean，根据该Bean的ID从BeanFactory中获取的实际上是FactoryBean的getObject()返回的对象，而不是FactoryBean本身，如果要获取FactoryBean对象，请在id前面加一个&符号来获取。

### 5.1.1 BeanFactory
BeanFactory是Spring框架的核心接口之一，它是一个用于管理和获取Bean（对象）的工厂。在Spring中，所有的Bean都由BeanFactory进行创建、管理和维护。它提供了一种机制，可以根据配置信息（通常是XML或注解配置）来实例化、初始化和管理Bean。BeanFactory是Spring的基础构建块之一，提供了许多功能，如延迟加载、依赖注入等。

`org.example.designPatterns.creationalPattern.simpleFactoryPattern.demo4`

#### 5.1.1.1 遵循严格的生命周期
通过BeanFactory创建一个Bean要经过非常严格的流程处理，很繁琐。
```
1.BeanNameAware's setBeanName
2.BeanClassLoaderAware's setBeanCLassLoader
3.BeanFactoryAware's setBeanFactory
4.EnvironmentAware's setEnvironment
5.EmbeddedValueResolverAware's setEmbeddedValueResolver
6.ResourceLoaderAware's setResourceLoader (only applicable when running in an application context)
7.ApplicationEventPublisherAware's setApplicationEventPublisher (only applicable when running i
context
8.MessageSourceAware's setMessageSource (only applicable when running in an application context)
9.ApplicationContextAware's setApplicationContext (only applicable when running in an application
10.ServletContextAware's setServletContext (only applicable when running in a web application conte
11.postProcessBeforeInitialization methods of BeanPostProcessors
12.InitializingBean's afterPropertiesSet
13.a custom init-method definition
14.postProcessAfterInitialization methods of BeanPostProcessors
On shutdown of a bean factory,the following lifecycle methods apply:
1.postProcessBeforeDestruction methods of DestructionAwareBeanPostProcessors
2.DisposableBean's destroy
3.a custom destroy-method definition
```
#### 5.1.1.2 Spring中使用
- 加载xml
```java
// 方法一：加载文件系统中的xml
Resource resource = new FileSystemResource("beans.xml");
BeanFactory factory = new XmlBeanFactory(resource);
// 方法二：加载classpath下的xml
ClassPathResource resource = new ClassPathResource("beans.xml");
BeanFactory factory = new XmlBeanFactory(resource);
// 方法三：获取ApplicationContext
ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml", "applicationContext-part2.xml"});
BeanFactory factory = (BeanFactory) context;
```
- 接着使用`getBean(String beanName)`方法就可以取得bean的实例；
```java
// 判断工厂中是否包含给定名称的bean定义，若有则返回true
boolean containsBean(String beanName)
// 返回给定名称注册的bean实例。根据bean的配置情况，如果是singleton模式将返回一个共享实例，否则将返回一个新建的实例，如果没有找到指定bean,该方法可能会抛出异常
Object getBean(String)
// 返回以给定名称注册的bean实例，并转换为给定class类型　
Object getBean(String, Class)
// 返回给定名称的bean的Class,如果没有找到指定的bean实例，则排除NoSuchBeanDefinitionException异常
Class getType(String name)
// 判断给定名称的bean定义是否为单例模式
boolean isSingleton(String)
// 返回给定bean名称的所有别名
String[] getAliases(String name)
```

#### 5.1.1.3 SpringBoot中使用
- Springboot主入口启动时，保存上下文
```java
@SpringBootApplication
public class RedisExampleApplication {
    public static void main(String[] args) {
        // 启动时，保存上下文，并保存为静态
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RedisexampleApplication.class, args);
        SpringContextUtil1.setAc(applicationContext);
    }
}

/**
 * spring获取bean通过保存ApplicationContext对象
 */
public class SpringContextUtil1 {
    private static ApplicationContext ac;
    public static <T>  T getBean(String beanName, Class<T> clazz) {
        T bean = ac.getBean(beanName, clazz);
        return bean;
    }

    public static void setAc(ApplicationContext applicationContext){
        ac = applicationContext;
    }
}
```
- 通过继承的spring提供的接口或者类
```java
// 通过实现ApplicationContextAware
@Component
public class SpringContextUtil3 implements ApplicationContextAware {
    private static ApplicationContext ac;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }
    public static <T> T getBean(Class<T> clazz) {
        T bean = ac.getBean(clazz);
        return bean;
    }
}


// 通过继承ApplicationObjectSupport
@Service
public class SpringContextUtil4 extends ApplicationObjectSupport {
    public <T> T getBean(Class<T> clazz) {
        T bean = getApplicationContext().getBean(clazz);
        return bean;
    }
}


//  通过继承WebApplicationObjectSupport
@Service
public class SpringContextUtil5 extends WebApplicationObjectSupport {
    public <T> T getBean(Class<T> clazz) {
        T bean = getApplicationContext().getBean(clazz);
        return bean;
    }
}

// 通过spring提供的静态方法获取上下文  通过WebApplicationContextUtils，适用于web项目的b/s结构
public class SpringContextUtil2 {

    public static <T> T getBean(ServletContext request, String name, Class<T> clazz){
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request);
        // 或者
        WebApplicationContext webApplicationContext1 = WebApplicationContextUtils.getWebApplicationContext(request);
//        webApplicationContext1.getBean(name, clazz)
        T bean = webApplicationContext.getBean(name, clazz);
        return bean;
    }
}

// 通过spring提供的静态方法获取上下文  通过ContextLoader获取WebApplicationContext，获取上下文


```


### 5.1.2 FactoryBean
FactoryBean是一个特殊的Bean，它实现了FactoryBean接口。它允许开发人员在配置文件中定义自己的工厂类，用于创建特定类型的Bean。当Spring容器遇到一个实现了FactoryBean接口的Bean定义时，它不会直接实例化这个Bean，而是使用FactoryBean接口的方法来获取实际的Bean实例。这样可以在创建Bean时执行一些自定义的逻辑。

FactoryBean是实现了FactoryBean接口的Bean，可以该Bean的ID从BeanFactory中获取的实际上是FactoryBean中getObject()方法返回的实例对象，而并不是直接FactoryBean本身，想要获取FactoryBean对象本身，可以在id前面加一个&符号来获取。

### 5.1.3 BeanFactory与FactoryBean
- BeanFactory是接口，提供了OC容器最基本的形式，给具体的IOC容器的实现提供了规范，
- FactoryBean也是接口，为IOC容器中Bean的实现提供了更加灵活的方式，FactoryBean在IOC容器的基础上给Bean
的实现加上了一个简单工厂模式和装饰模式(如果想了解装饰模式参考：修饰者模式(装饰者模式，Decoration) 我们可以在getObject()方法中灵活配置。其实在Spring源码中有很多FactoryBean的实现类.



### 5.1.4 BeanFactory与ApplicationContext
- BeanFacotry是spring中比较原始的Factory。如XMLBeanFactory就是一种典型的BeanFactory。原始的BeanFactory无法支持spring的许多插件，如AOP功能、Web应用等。 
- ApplicationContext接口,它由BeanFactory接口派生而来，ApplicationContext包含BeanFactory的所有功能，通常建议比BeanFactory优先
- ApplicationContext的中文意思是“应用前后关系”，它继承自BeanFactory接口，除了包含BeanFactory的所有功能之外，在国际化支持、资源访问（如URL
和文件）、事件传播等方面进行了良好的支持，被推荐为Java EE应用之首选，可应用在Java APP与Java Web中。
- ApplicationContext会自动检查是否在定义文件中有实现了BeanPostProcessor接口的类， 如果有的话，Spring容器会在每个Bean(其他的Bean
)被初始化之前和初始化之后， 分别调用实现了BeanPostProcessor接口的类的postProcessAfterInitialization()方法 和postProcessBeforeInitialization()方法 之所以用z开头是因为 初始化有顺序




# 参考
- [~~springboot获取bean的几种常用方式~~](https://bbs.huaweicloud.com/blogs/360056)
- [~~三分钟快速了解Spring中的工厂模式~~](https://juejin.cn/post/6992716383893061663)

用Spring实现工厂模式，简单实用
https://blog.csdn.net/u011291072/article/details/120296086
Spring Boot中使用注解实现简单工厂模式
https://www.cnblogs.com/east7/p/13412365.html
工厂模式-理解SPRING的BEAN工厂（马士兵经典例子）
https://www.cnblogs.com/lihaoyang/p/13720163.html
Spring中的设计模式：工厂方法模式
https://segmentfault.com/a/1190000022276517


