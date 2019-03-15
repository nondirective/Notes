# JSP

## 一、JSP指令

使用JSP指令的基本语法为`<%@ 指令 属性名=“值” %>` 



共有一下三大类指令：

* page指令
* include指令
* taglib指令

### page指令

在一个页面当中无论page指令出现在哪个位置，它都是对整个页面起到作用的

所有page指令的语法：

```jsp
<%@ page 
    [ language="java" ] 
    [ extends="package.class" ] 
    [ import="{package.class | package.*}, ..." ] 
    [ session="true | false" ] 
    [ buffer="none | 8kb | sizekb" ] 
    [ autoFlush="true | false" ] 
    [ isThreadSafe="true | false" ] 
    [ info="text" ] 
    [ errorPage="relative_url" ] 
    [ isErrorPage="true | false" ] 
    [ contentType="mimeType [ ;charset=characterSet ]" | "text/html ; charset=ISO-8859-1" ] 
    [ pageEncoding="characterSet | ISO-8859-1" ] 
    [ isELIgnored="true | false" ] 
%>
```

#### errorPage

`errorPage="/error.jsp"`

当当前页面出现错误时，跳转到errorPage设置的错误处理页

如果url开头无  **/**  则代表目录基于当前文件目录

开头有  /  则代表目录基于web应用目录



`isErrorPage=“true | false”` 

说明当前页面是否为错误处理页

如果这个属性的值为true时，这个页面会拥有一个Exception对象，其中包含了错误页当中的错误信息

#### 为整个web应用设置errorPage

在web.xml文件当中，<web-app>标签内

  ```jsp
<error-page>
  <error-code>指定触发该错误的的错误状态码</error-code>
  <location>错误页面的路径</location>
  <exception-type>指定的触发该错误页面的异常类</exception-type>
 </error-page>
  ```

#### 关于在web.xml当中用<error-page>为整个web应用设置的错误处理页在IE下无法跳转的解决方法

只要将错误处理页的文件大小增加到1024k以上就可以正常跳转

### include

用于引入多个jsp页面进行合成

使用方法：`<%@include file="filePath%>`

## 二、九大JSP内置对象

| No.  |  内置对象   |                  类型                  |
| :--: | :---------: | :------------------------------------: |
|  1.  | pageContext |     javax.servlet.jsp.PageContext      |
|  2.  |   request   | javax.servlet.http.HttpServletRequest  |
|  3.  |  response   | javax.servlet.http.HttpServletResponse |
|  4.  | application |      javax.servlet.ServletContext      |
|  5.  |   config    |      javax.servlet.ServletConfig       |
|  6.  |     out     |      javax.servlet.jsp.JspWriter       |
|  7.  |    page     |            java.lang.Object            |
|  8.  |  exception  |          java.lang.Throwable           |
|  9.  |  sesssion   |    javax.servlet.http.HttpSesssion     |

其中的application对象就是前面所说的ServletContext对象，是Web应用的

在这一部分将对pageContext,page,out作介绍

### 内置对象使用说明

#### page对象

代表当前的jsp页面的对象

#### out对象

相当于Servlet当中的PrintWriter对象，使用方法也极其相似

#### pageContext对象

pageContext自己为一个域对象之外，还有访问其他域对象的能力，同时还能通过pageContext获得其他八个内置对象的隐式对象

##### 获得影其他八个内置对象

```jsp
<%
pageContext.getSession();  //session
pageContext.getRequest();  //request
pageContext.getServletContext();  //application
pageContext.getResponse();  //resposne
pageContext.getException();  //exception
pageContext.getPage();  //page
pagecontext.getServletConfig();  //config
pagecontext.getOut();  //out
%>
```

##### pageContext对象作为域对象使用

```jsp
<%
pageContext.setAttribute(String attributeName,Object value);
pageContext.getAttribute(String attributeName,Object value);
pageContext.remoteAttribute(String attributeName);
pageContext.findAttribute(String attributeName);
%>
```

其中的前三个方法是对属性的添加、获取、移除操作

而`findAttribute(String attributeName)` 方法是按照一定的域对象顺序查找属性

*findAttribute*方法对属性值查找的域对象顺序

page->request->session->application

如果按照这个顺序查找属性值没有结果的话，那么*findattribute* 方法将返回null

##### 通过pageContext对象访问其他域对象

```jsp
<%
pageContext.setAttribute(String attributeName,Object value,int scope);
pageContext.getAttribute(String attributeName,int scope);
pageContext.remoteAttribute(String attributeName,int scope);
%>
```

访问其他对象的时候，只需要在原先的get,set,remote方法的基础上传入多一个Scope参数代表操作的域对象

Scope代表的域对象

```
pageContext.PAGE_SCOPE
pageContext.REQUEST_SCOPE
pageContext.SESSION_SCOPE
pageContext.APPLICATION_SCOPE
```

##### 通过pageContext对象进行请求转发

这种转发方式在实际开发中不常用，了解

通常情况下使用<<jsp:forward>>标签

```jsp
<%
pageContext.forward(目标页面);
%>
```

