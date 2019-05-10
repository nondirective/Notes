项目启动 

 	1. [start project](start.spring.io)  下载项目文件
 	2. intellij idea 导入maven项目,选中`pom.xml` 导入



```java
//标记说明这个类为Controller类
@Controller
public class MyController {
    /**
    *RequestMapping
    *映射请求url路径
    *其中{xxx}用于接受path参数
    *
    *ResponseBody
    *将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区
    */
    
    @RequestMapping(path = {"/index/{groupId}/{userId}/","/{groupId}/{userId}/"})  //映射多个url
    //映射单个url @RequestMapping("/index")
    @ResponseBody
    public String index(@PathVariable(value = "groupId")String groupId,  //路径当中的参数
                        @PathVariable(value = "userId")int userId,       //路径当中的参数
                        @RequestParam(value = "key",defaultValue= "key")String key,         //请求当中的参数
                        //defaultValue 如果请求当中该参数为空则制指定默认值
                        @RequestParam(value = "value",defaultValue = "value")String value){ //请求当中的参数
            StringBuilder sb = new StringBuilder();
            sb.append("Group Id:");
            sb.append(ln(groupId));
            sb.append("User Id:");
            sb.append(ln(String.valueOf(userId)));
            sb.append("Key:");
            sb.append(ln(key));
            sb.append("Value:");
            sb.append(ln(value));
        return sb.toString();
    }
    
    public static String ln(String str){
        return str+"</br>";
    }
}
```

使用标注读取Cookie的值:

```java
@RequestMapping("/readCookie")
@ResponseBody
//在@CookieValue标注当中
//value指的是cookie的名称
//defaultValue指的是没有找到指定Cookie的时候的默认值
public String readCookie(@CookieValue(value="username",defaultValue="null")String username){
    return "User Name:"+username;
}
```



项目结构

​	src

​	----main(项目主体)

​	--------java(Java文件目录)

​	--------resource(资源文件目录)

​	------------static(静态文件)

​	------------templet(模板)



状态码301,302

301:永久跳转

​	使用301跳转之后浏览器就会记住这次重定向下次再访问上次重定向发起的path的时候就不会再使用这个path向服务器发起请求，而是直接向重定向后的path发起请求

302:临时跳转

​	说明这一次只是临时的需要跳转，下次访问上次重定向发起的path 的时候还是要重新的以这个path向服务器发起请求





IoC(Inversion of Control)控制反转

通过控制反转，对象在被创建的时候，由一个调控系统内所有对象的外界实体将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。

软件工程中对象之间的耦合度就是对象之间的依赖性。指导使用和维护对象的主要问题是对象之间的多重依赖性。对象之间的耦合越高，维护成本越高。

 DI (Dependency inject)依赖注入

当前对象需要哪些对象，将对象注入到当前对象中





在resource/static目录下创建Spring config xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="PersonId" class="com.example.demo.Person">
    <!--Person p = new Person();-->
	<!--id为使用时引入bean时使用的ID，class时bean的完整类名 -->
		<property name="Computer" ref="ComputerId">
		<!--Person.setComputer(c);
		</property>
		
    </bean>
    
    <bean id="ComputerId" class="com.example.demo.Computer">
    <!--Computer c = new Computer();-->
    </bean>
</beans>
```
在xml配置文件当中说明IoC和DI
`<bean>` 标签说明这个bean是控制反转的，即要拿到实例需要从容器中获取
`<property>`标签说明这个属性是依赖注入的，bean中依赖这个对象，需要容器把实例注入到这个bean中
在上面的beans.xml文件当中
bean_PersonId 说明Person这个对象需要容器当中获取
bean_ComputerId说明Computer这个对象需要从容器当中获取
property_Computer 说明这个bean依赖一个属性Computer从ComputerId当中获取
如果不能理解可以参考xml文件当中的等价java代码

然后，需要获得对象的实例就要得到BeanFactory，从BeanFactory当中得到对象实例
BeanFactory又简单的分成两种方式

1. 需要用到对象实例的时候才创建对象实例

```java
@RequestMapping("/getPersonMsg")
@ResponseBody
public String getPersonMsg(){
    String xmlPath = "static/beans.xml";
    org.spring.core.io.Resourece resource = new ClassPathResourece(xmlPath);
    BeanFactory beanFactory = new org.springframework.beans.factory.xml.XmlBeanFactory(resource);
    Person p = (Person)applicationContext.getBean("PersonId");
    return "name:"+p.getName()+";sex:"+p.getSex();
}
```
2. 在BeanFactory初始化的时候就把对象的实例创建好

这种方式的BeanFactory有两种常用的实现类，FileSystemXmlApplicationContext和ClassPathXmlApplicationContext。分别是基于文件系统绝对路径的和基于classpath相对路径的
在这里就以ClassPathXmlApplicationContext为例子

```java
@RequestMapping("/getPersonMsg")
@ResponseBody
public String getPersonMsg(){
    String xmlPath = "static/beans.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
    Person p = (Person)applicationContext.getBean("PersonId");
    return "name:"+p.getName()+";sex:"+p.getSex();
}
```
先引入ApplicationContext对象后再使用applicationContext对象的getBean()方法就可以拿到实例

静态工厂

1. 创建工厂类，设置一个获取目标bean的静态方法
2. 把bean设置到bean.xml,比普通构造`<bean id="PersonId" class="com.example.demo.controller.Person">` 的不同之处是，class指定的将是beanFactory的路径，同时在指定beanFactory当中的获得bean的静态方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="PersonId" class="com.example.demo.Person" factory-mehtod="getPerson">
    </bean>
</beans>
```



实例工厂

实例工厂的bean配置方式是把一个bean工厂的实例交给spring，我们向spring请求需要一个对象的时候，通过spring返回一个对象

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="BeanFactoryId" class="com.example.demo.myBeanFactory"></bean>
    <!--工厂对象-->
    <bean id="PersonId" class="com.example.demo.Person" factory-bean="BeanFactoryId" factory-method="getPerson"></bean>
    <!--Person对象，说明要获得他需要通过的工厂，和在工厂中要使用到的get方法-->
</beans>
```

bean的作用域

在xml配置bean的时候加入scope属性

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
<bean id="BeanFactoryId" class="com.example.demo.myBeanFactory"></bean>
<bean id="PersonId" class="com.example.demo.Person" factory-bean="BeanFactoryId" factory-method="getPerson" scope="singleton"></bean>
</beans>
```

singleton:每次请求获取的都是同一个实例

prototype:一次请求生成一个实例

request:一次请求共用一个实例

session:一次回话共用一实例

