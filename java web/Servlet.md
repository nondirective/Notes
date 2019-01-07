#                                                                                                                                                                                                                                                                                                                Sevlet

## 链接Servlet程序

编辑项目文件夹下的/WEB-INF/web.xml                     

在根标签下插入

```xml
<servlet>
	<servlet-name>Hello</servlet-name>
    <!--随意取名但是要和<servlet-mapping>中的<servlet-name>相同-->
	<servlet-class>A_Servlet</servlet-class>
    <!--servlet程序的类名-->
    
</servlet>

<servlet-mapping>
	<servlet-name>Hello</servlet-name>
	<url-pattern>/A_Servlet</url-pattern>
	<!--访问servlet程序用到的路径-->
</servlet-mapping>
```

## 插入初始化参数

```xml
<servlet>
	<servlet-name>Hello</servlet-name>
    <!--随意取名但是要和<servlet-mapping>中的<servlet-name>相同-->
	<servlet-class>A_Servlet</servlet-class>
    <!--servlet程序的类名-->
    
    <!--初始化参数-->
    <init-param>
    	<param-name>Name</param-name>
        <param-value>Hari</param-value>
    </init-param>
</servlet>

<servlet-mapping>
	<servlet-name>Hello</servlet-name>
	<url-pattern>/A_Servlet</url-pattern>
	<!--访问servlet程序用到的路径-->
</servlet-mapping>
```



## 生命周期

一个Servlet程序必须实现Servlet接口或者继承Servlet的实现类，如HttpServlet

```java
import javax.servlet.*;
import javax.servlet.http.*;

public class Servlet_Demo implements     Servlet{
    public void init(){}
    
    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {}
    
    private void destory() {}
}
```



要使用到程序的时候会调用一次且只调用一次init()方法作初始化

外部向程序发送请求的时候调用service()方法提供服务

当程序结束的调用destory()方法

Servlet程序的绝大部分方法都是容器（Tomcat）自动调用的

在覆盖生命周期方法的时候如果实现的类是GenericServlet及以下的派生了，对init()方法复写的时候切记复写无参数的init()方法,因为有参数的init()方法是tomcat容器调用的初始化方法,其中做了必要的初始化操作

```java
public void init(ServletConfig config) throws ServletException {
	this.config = config;
    init();
}
```



## Servlet对象

### ServletConfig对象

这个对象当中存储的是配置文件信息也就是在/WEB-INF/web.xml当中配置的servlet注册信息，以及其中配置的初始化参数

#### 获得初始化参数

```java
public void init(ServletConfig config) throws ServletException {
		Enumeration e = config.getInitParameterNames();
		while(e.hasMoreElements()) {
			System.out.println(config.getInitParameter((String) e.nextElement()));
		}
    
    	//获得servlet配置信息当中的名称
    	//<servlet>
		//	<servlet-name>Hello</servlet-name>
    	System.out.println(config.getServletName());
	}
```

### ServletContext对象

ServletContext对象是一个Web应用程序当中所有servlet对象共享的一个对象，用它可以实现Servlet对象之间的通讯,利用它能够把一个Servlet对象得到的request请求转发到另外一个Servlet对象

#### 在xml配置文件当中给ServletContext对象设置参数

```xml
<context-param>
	<parma-name>name</parma-name>
    <param-value>hari</param-value>
</context-param>
```

#### 给ServletContext对象设置属性

```java
config.getServletContext().setAttribute("name","hari");
```

### HttpServlet对象

#### HttpServlet 对象的原理

客户端发送请求到HttpServlet对象

调用service(ServletRequest req, ServletResponse rep)方法

该方法内部调用service(HttpServletRequest req, HttpServletResponse)方法

```java
public void service(ServletRequest req, ServletResponse rep){
    service((HttpServletRequest)req,(HttpServletResponse)rep);
}
```

service(HttpServletRequest req, HttpServletResponse)方法根据请求的类型选择调用doGet(),doPost()方法

下面用假代码打个比方

```java
public void service(HttpServletRequest req, HttpServletResponse){
    switch(req.type){
        case "get":
            doGet();
       		break;
        case "pose":
            doPost();
            break;
    }
}
```

所以说我们编辑HttpServlet类只需要复写相应请求的do方法即可

如果doGet(),doPost()方法没有复写就被调用，会出现405错误(没有相应的处理请求的方法)







#### 处理get请求

举例

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("text/html");  //设置响应头的类型
	String firstName = req.getParameter("firstName");  //获得get请求当中名为firstName的值
	String lastName = req.getParameter("lastName");  //获得get请求当中名为lastName的值
	PrintWriter pw = resp.getWriter();  //获得响应头输出流
	pw.println("<h1>"+message+"</h1>");
	pw.println("<p>"+firstName+" "+lastName+"</p>");
}
```

```html
<!DOCTYPE html>
<html>
<head>
<title>Servlet test</title>
</head>
<body>
	<h1>Servlet test</h1>
	<form action="A_Servlet" method="GET">
        <!--属性action的值就是目标servlet程序名-->
		<a>First name:</a> <input type="text" name="firstName" /> <br/> 
		<a>Last name:</a> <input type="text" name="lastName" /> <br/>
		 <input type="submit" value="Submit" />
	</form>
</body>
</html>
```

#### 处理Post请求

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("text/html");  //设置响应头的类型
	String firstName = req.getParameter("firstName");  //获得get请求当中名为firstName的值
	String lastName = req.getParameter("lastName");  //获得get请求当中名为lastName的值
	PrintWriter pw = resp.getWriter();  //获得响应头输出流
	pw.println("<h1>"+message+"</h1>");
	pw.println("<p>"+firstName+" "+lastName+"</p>");
}

protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
    doGet(req, resp);
}
```

```html
<!DOCTYPE html>
<html>
<head>
<title>Servlet test</title>
</head>
<body>
	<h1>Servlet test</h1>
	<form action="A_Servlet" method="POST">
        <!--属性action的值就是目标servlet程序名-->
		<a>First name:</a> <input type="text" name="firstName" /> <br/> 
		<a>Last name:</a> <input type="text" name="lastName" /> <br/>
		 <input type="submit" value="Submit" />
	</form>
</body>
</html>
```

### Filter

#### 在xml文件当中配置Filter

```xml
<filter>
  <filter-name>LogFilter</filter-name>
    <!--和servlet配置相同-->
  <filter-class>LogFilter</filter-class>
    <!--Filter类的路径-->
  <init-param>
    <param-name>Site</param-name>
    <param-value>w3cschool在线教程</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>LogFilter</filter-name>
    <!--和servlet配置相同-->
  <url-pattern>/*</url-pattern>
    <!--接受过滤的目标-->
</filter-mapping>
```

#### Filter类

```java
public class LogFilter implements Filter{
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("Filter init.");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			System.out.println(new Date().toString());
        
        	/*
        	*执行完一系列操作之后将请求回传到过滤链当中
        	*如果没有这行操作，请求将在这里中断
        	*/
			chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		System.out.println("Filter destory.");
	}
}

```

#### 使用多个Filter

 在一个项目当中能够根据实际情况定义多个过滤器，只需要在web.xml文件当中正确链接即可

此外，过滤器的应用顺序是和<filter-mapping>的顺序相同的