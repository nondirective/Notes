# JSTL核心标签库

## 核心标签库简介

JSTL的核心标签库标签共13个，使用这些标签能够完成JSP页面的基本功能，减少编码工作。

　　从功能上可以分为4类：表达式控制标签、流程控制标签、循环标签、URL操作标签。

1. 表达式控制标签:out标签、set标签、remove标签、catch标签
2. 流程控制标签:if标签、choose标签、when标签、otherwise标签
3. 循环标签:forEach标签、forTokens标签
4. URL操作标签:import标签、url标签、redirect标签、param标签

　　在JSP页面引入核心标签库的代码为：` <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>` 

## 标签库的安装

tld文件:从JSTL标签库的二进制包中的/tld目录下找到c.tld文件导入到项目当中

lib文件:从JSTL标签库的二进制包中的/lid目录下的两个tar文件导入到项目当中

## 表达式控制标签

### `<c:out>` 标签的使用

语法1:`<c:out value="需要输出到页面中的数据"/>`

语法2:`<c:out value="需要输出到页面中的数据" default="value当中的值为空时输出的默认值"/>`

　JSTL的使用是和EL表达式分不开的，EL表达式虽然可以直接将结果返回给页面，但有时得到的结果为空，`<c:out>`有特定的结果处理功能，EL的单独使用会降低程序的易读性，建议把EL的结果输入放入`<c:out>`标签中

### `<c:set>` 标签的使用

set标签可以用于javaBean对象的赋值和存储数据到域对象当中

#### 用法1:存储数据到域对象中

```jsp
<c:set value="page" var="scope1" scope="page"/>
<c:set var="scope1" scope="page">page</c:set>
```

value属性或标签体内容为要存储和的数据

var为存储数据变量名

scope为存储数据的域对象

#### 用法2:给JavaBean对象设置属性值

```jsp
<c:set value="Hari" target="${person}" property="name"/>
<c:set target="${person}" property="name">Hari</c:set>
```

标签体内容或value属性内容为要存储的数据

target为bean对象

property为要存储数据的bean对象属性

### `<c:remove>` 标签的使用

remove标签的功能是移除某个域对象当中的属性值

```jsp
<c:set var="name" value="hari" scope="page"/>
<c:out value="${name}"/>
<c:remove var="name" scope="page"/>
<c:out value="${name}">目标内容为空</c:out>
```

其中scope属性是可选的

### `<c:catch>` 标签的使用

catch标签体内如果有异常抛出，catch标签能够捕捉抛出的异常，并且把异常信息存储在var属性指定的变量当中

```jsp
<c:catch var="var_error">
...
</c:catch>
<c:out value="${var_error}"/>
```

## 流程控制标签

### `<c:if>` 标签的使用

#### 用法1:无标签体内容

```jsp
<c:if test"表达式" var="存储表达式结果的变量名" scope="域对象"/>
```

如果是没有标签体内容的if 标签test属性和var属性是必须的，scope属性是可选的

#### 用法2:有标签体内容

```jsp
<c:if test="表达式" var="存储表达式结果的变量名" scope="域对象">
...
</c:if>
```

var属性和scope属性都是可选的

如果test属性的表达式结果为真则执行标签体内容

### `<c:choose><c:when><c:otherwise>` 标签的使用

使用choose、when、otherwise三个标签能够构成if…else if…else的选择结构

```jsp
<c:choose>
	<c:when test="表达式1">
    ...
    </c:when>
    <c:when test="表达式2">
    ...
    </c:when>
    <c:otherwise>
    ...
    </c:otherwise>
</c:choose>
```



## 循环标签

### `<c:foreach>`  标签的使用

```jsp
<c:foreach
           var="遍历时用到的存值变量"
           items="表里的序列"
           begin="开始索引"
           end="结束缩影"
           step="循环步长"
           varStatus="存储循环中信息的变量">
...
</c:foreach>
```

varStatus的变量当中有四个状态属性，分别为

1. index 当前循环的索引
2. count  循环的次数
3. first  是否为第一个位置
4. last  是否为最后一个位置

使用实例:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Test</title>
</head>
<body>
<%
List<String> list = new ArrayList<String>();
list.add("第一");
list.add("第二");
list.add("第三");
pageContext.setAttribute("list",list);
%>

<c:forEach
	var="i"
	items="${list }"
	varStatus="status">
	当前的索引：<c:out value="${status.index }"/>
	<br/>
	已经循环的次数：<c:out value="${status.count }"/>
	<br/>
	是否为遍历开始：<c:out value="${status.first }"/>
	<br/>
	是否为遍历结尾：<c:out value="${status.last }"/>
	<br/>
    <br/>
</c:forEach>
</body>
</html>

<!--
页面结果:

当前的索引：0 
已经循环的次数：1 
是否为遍历开始：true 
是否为遍历结尾：false 

当前的索引：1 
已经循环的次数：2 
是否为遍历开始：false 
是否为遍历结尾：false 

当前的索引：2 
已经循环的次数：3 
是否为遍历开始：false 
是否为遍历结尾：true 
-->
```
