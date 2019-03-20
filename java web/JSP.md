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

#### page对象s

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

```jsp
<%
<jsp:forward page="xxxx.jsp"
%>
```

## 三、Jsp属性范围

这里属性范围值得是域对象保存的属性作用的范围

可以分为以下四类

* page属性范围(pageContext)
* request属性范围(request)
* session属性范围(session)
* application属性范围(application)

### page属性范围

page属性范围的属性只能作用于当前页面，跳转到其他页面之后就没有了

`pageContext.setAttribute("name","Nondreictional");` 

### request属性范围

request属性范围的属性可以作用于请求链当中的每一个页面，但是页面跳转的方式应该是转发，如果是使用超链接跳转则不在属性范围内

`request.setAttribute("name","Nondirectional");` 

### session属性范围

session属性范围作用于一个用户，一次会话，如果session对象没有被销毁，在当中设置的属性都是可以访问的

`session.setAttribute("name","Nondirectional");` 

application属性范围

设置在服务器当中，如果tomcat不重启的话，属性都是存在的可以访问的

`application.setAttribute("name","Nondirectional");` 

## 四、自定义Jsp标签

### 第一步，创建自定义标签类

直接使用自定义标签类的实现类``SimpleTagSupport` 

```java
package com.nondirectional.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class IpTag extends SimpleTagSupport {
	@Override
    //doTag()里面的内容就是自定义标签的行为
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext)this.getJspContext();
		JspWriter out = pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String ip = request.getRemoteAddr();
		out.print(ip);
	}
}

```

### 第二步，创建自定义标签库文件(.tld文件)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"
    version="2.0">
    <!--taglib标签的内容可以从apache的webapps/example/当中吵到一个例子的tld文件复制粘贴-->
    <!--对标签库的描述-->
    <description>A tag library exercising SimpleTag handlers.</description>
    <tlib-version>1.0</tlib-version>
    <short-name>CT</short-name>
    
    <!--链接到这个标签库用到的uri-->
    <uri>http://www.nondirectional/jsp/tags/</uri>
    
    <!--下面就是自定义标签的信息-->
    <tag>
    <description>Tag</description>
    <!--标签名-->
    <name>ViewIP</name>
    <!--所使用到的自定义标签类的完全类名-->
    <tag-class>com.nondirectional.taglib.IpTag</tag-class>
    <!--有无标签体-->
    <body-content>empty</body-content>
    </tag>
</taglib>
```



### 第三部，在.jsp文件当中引入标签库

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--prefix是标签库的前缀名名称-->
<%@ taglib uri="http://www.nondirectional/jsp/tags/" prefix="ct" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<ct:ViewIP/>
</body>
</html>
```

### 获取标签体内容并修改

```java
package com.nondirectional.taglib;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.swing.text.AbstractDocument.Content;

public class MyTag extends SimpleTagSupport{
	@Override
	public void doTag() throws JspException, IOException {
		JspFragment jspFragment = this.getJspBody();
		StringWriter sw = new StringWriter();
        //把标签体内容传到StringWriter流当中
		jspFragment.invoke(sw);
      	//转换成字符串数据
		String content = sw.toString();
		content = content.toUpperCase();
		//回写到标签体
 		jspFragment.getJspContext().getOut().write(content);
	}
}
```



### 自定义标签控制页面部分的执行与否和循环

使标签体里面的内容不执行

 ```java
package com.nondirectional.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class MyTag extends SimpleTagSupport{
	@Override
	public void doTag() throws JspException, IOException {
		JspFragment jspFragment = this.getJspBody();
                //如果想要标签体里面的内容不执行，只要把下面一个行注释掉就可以了
//			jspFragment.invoke(null);
	}
}
 ```

让标签体内容循环执行

```java
package com.nondirectional.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class MyTag extends SimpleTagSupport{
	@Override
	public void doTag() throws JspException, IOException {
		JspFragment jspFragment = this.getJspBody();
		//把invoke方法放入循环当中
        for(int i=0;i<5;i++){
            jspFragment.invoke(null);
        }
	}
}
```

