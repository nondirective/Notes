# IOC&DI

 配置bean到容器



配置Configuration类（配置类)，等价于在xml方式当中配置xml配置文件

@Configuration注解标注



配置Bean

@Bean标注返回bean的方法，可以传入参数指定bean的name

如果不指定则方法名就是bena的name



ApplicationContext使用实现类AnnotationConfigApplicationContext，构造参数为配置类

获取bean 依旧使用getBean方法





MainConfig.java

```java
@Configuration
public class MainConfig{
    @Bean("person")				//参数为指定bean的name
    public Person getPerson(){
        return new Person("Hari",12);
    }
}
```

MainTest.java

```java
public class MainTest{
    public void main(String args[]){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        Person p = (Person)ctx.getBean("person");
    }
}
```





包扫描



在配置类当中加入@ComponentScan注解，

value指定扫描的包

excludeFilters指定排除扫描类的过滤规则

includeFilters指定包含扫描类的过滤规则

![](..\spring-xml\include exclude-filter过滤表达式.png)

常用两种依旧为annotation和assinable

注意在使用includeFilters时需要把默认的过滤规则关闭,即设置ComponentScan注解的useDefaultFilters为false

excludeFilters为用例

```java
@ComponentScan(value="com.nond"
              excludeFilters={
                  @Filter(type = FilterType.ANNOTATION,classes = Service.class)  //排除所有标注@Service注解的类
              	}
              )
public class MainConfig{
    ...
}
```

@Filter标签内Type为过滤类型，使用FilterType当中的静态常量，

使用annotation、assinable、custom这三种使用类过滤的，第二个属性使用classes

使用aspectj、regex这两种使用表达式过滤的，第二个属性使用pattern

标注bean 和获取bean和使用xml 配置一致



如果需要配置多种扫描策略

还可以使用@ComponentScans注解，该注解接受@ComponentScan[]数组，配置多种扫描策略





CUSTOM方式过滤



即使用自定义的过滤规则进行过滤

`@Filter`注解当中type使用``FilterType.CUSTOM`静态常量，classes传入自定义的匹配规则类即可





创建自定义匹配规则类

实现**TypeFilter**接口

实现接口的`public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)`方法



通过参数**metaReader**可以获得当前扫描中的类的注解信息数据、类信息数据、类资源路径信息数据，分别使用`getAnnotationMetadata()`、`getClassMetadata()`、`getResource()`方法获取



通过**metadataReaderFactory**参数的`getMetadataReader()`方法还能够获取到其他类的`MetadataReader`



其中返回值为是否匹配成功



bean scope



四种作用域



* `ConfigurableBeanFactory.SCOPE_PROTOTYPE`多实例

* `ConfigurableBeanFactory.SCOPE_SINGLETON`单实例
* `WebApplicationContext.SCOPE_REQUEST`请求
* `WebApplicationContext.SCOPE_SESSION`会话



配置Bean的作用域



`@Scope`注解，在方法或类使用





懒加载



单实例bean默认在容器创建时创建

多实例bean在需要取用bean时才创建



针对单实例bean，可以使用@Lazy注解标注使之懒加载，即取用bean时创建







conditional条件注入



满足条件时才把bean注入到容器当中

使用@Conditional注解标注

该注解接受一个Condition接口数组



实现Conndition接口



复写match()方法

通过方法的两个参数能够获取，运行环境信息和注解信息

match方法的返回值为 boolean，决定条件是否成立





导入bean到容器当中

使用import 注解

@Import(value = {bean.class})



使用这样方法导入的bean只能为无参构造器构造的





使用ImportSelector接口导入

实现public String[] selectImports(AnnotationMetadata importingClassMetadata)方法

返回值为全类名列表

import注解通过全类名列表导入这些类的无参构造器创建的bean 



使用ImportBeanDefinitionRegistrar接口

public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)

创建BeanDefinition使用实现类RootBeanDefinition

调用registry.registerBeanDefinition("beanID"，BeanDefinition)





其中使用importingClassMetaData能够获取到使用Import注解标注类的类信息







创建工厂bean

实现FactoryBean接口



```java
package com.nond.demo02;

import org.springframework.beans.factory.FactoryBean;

public class PersonFactoryBean implements FactoryBean{

	public Object getObject() throws Exception {
		return new Person("Hari",16);
	}

	public Class getObjectType() {
		return Person.class;
	}

	/**
	 * 该方法的返回值决定使用FactoryBean创建的Bean是否是单例的
	 * true：创建的Bean为单例
	 * false:创建的Bean为多例
	 */
	public boolean isSingleton() {
		return false;
	}
	
}
```

配置到Config类

```java
	@Bean
	public PersonFactoryBean person() {
		return new PersonFactoryBean();
	}
```





如果需要获得FactoryBean本身，只需要在获取Bean的时候在id前加&即可



给Bean指定初始化(Bean创建并且赋值完成后)和销毁(Bean销毁之前)方法

1. 在Bean 注解当中指定init-method属性和destory-method属性
2. Bean类实现DisposableBean,InitializingBean接口，分别对应销毁和初始化
3. 使用PostConstruct,PreDestory注解标注bean的初始化销毁方法





beanProcessor   bean后处理器

bean初始化方法调用前后处理

该处理器面向所有的容器当中的bean



创建及使用

实现PostBeanProcessor接口

实现其两个方法

将该bean装配到容器当中



postBeanProcessor的流程



创建bean

bean赋值

postProcessBeforeInitialization

bean初始化

postProcessAfterInitialization



如果在postProcessBeforeInitialization返回的bean是null,流程终止，直接跳出返回null



在Spring底层当中大量的使用到了BeanProcessor，如在初始化前后，bean 属性注入，组件注入等





@Value标签



该标签的作用是用于给属性赋值

标签能够传入基本数据类型

能够使用Spel表达式#{}

还能获取到资源文件当中的信息(运行环境的环境变量)${}





获取properties文件属性

@PropertySource注解在MainConfig当中说明引入的资源文件

然后在@Value标签使用

或者用运行环境获取环境变量方式获取

Environment environment = ctx.getEnvironment();

environment.getProperty("person.name")







@Autowired自动装配



使用@Autowired标注的属性，自动在容器当中寻找类型相匹配的bean装配

如果在容器当中，类型匹配的bean有多个，默认情况下注入的是和属性名相同的bean

还可以使用@primary注解标注bean，说明在有多个类型匹配的bean情况下，首选使用使用该bean注入

还可以使用@Qualifier注解标注需要自动装配的bean，说明一定要注入名为@Qualifier value  值的bean

如果在容器当中没有找到bean则不需要注入，则可以使用@autowired注解的required属性，false为该属性在没有找到能够注入的bean时不注入，如果没有指定这个注解的属性，在没有找到bean的情况下会报错





除了Autowired注解以外还有@Resource(JSR250),@Inject(JSR330)

即对JCP规范的支持

@Resource注解

​	通过设置name,type两个属性能够确定寻找bean的范围

​	name属性，寻找容器当中ID和name属性值匹配的bean，如果不存在，报错

​	type,属性，寻找容器当中类型相匹配的bean，如果不存在或者存在多个，报错

@Inject注解

​	和autowired相比缺少了required=false功能，需要导入javax.inject包





@Autowired注解不止能够标注在属性上

还能够标注在构造方法上setter方法上，以及标注在方法参数上





注意点1

如果被标注需要装配到容器当中的类，该类只有一个有参构造器且其中参数需要容器当中取用

则在这种情况下可以不使用@Autowired注解标注也能够自动装配



注意点2

在mainconfig当中用@Bean标注方法装配的bean

参数需要自动装配，这种情况下也可以不使用@autowire的标注也会自动装配





自动装配Spring底层的bean



需要装配Spring底层bean的类实现xxxxxAware接口

实现接口的方法，通过其中的参数就能拿到底层的bean



通过这种方式可以拿到字符串解析器，容器等对象

这种实现接口自动装配的方法也是通过PostBeanProcessor方式实现的





@Profile的使用



在生产过程中有多种程序的运行环境，如开发环境，测试环境，生产环境

在多种生产环境之下需要暴露的bean也不尽相同，比如各种环境下的数据源



使用profile能够给bean指定暴露在哪种运行环境下

四种环境

default默认

dev开发

test测试

prod生产



没有使用profile标注的bean，在任何情况下都能够使用



还能够给config类标注指定config的运行环境





改变运行环境



1

设置运行参数-Dspring.profiles.active=xxx

2

通过applicationContext设置运行环境

通过无参构造器创建一个空的applicationContext

applicationContext.getEnvironment().setProfiles(args...)能够同时设置多个运行环境

applicationContext.register(mainconfig.class)把主配置类注册进去

applicationContext.refresh()刷新applicationContext



