# EL表达式和EL函数库的使用

## EL表达式

使用EL表达式获取数据的语法:`${标识符}`

EL表达式获取数据会使用到`pageContext.findAttribute();`

如果没有找到指定的数据EL表达式将会返回一个**空字符串** ，而非null

EL表达式可以很轻松获取JavaBean的属性，或获取数组、Collection、Map类型集合的数据

### EL表达式获取数据实例

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.nondirectional.domain.Person,java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	Person p1 = new Person();
	Person p2 = new Person();
	p1.setName("P1");
	p2.setName("P2");
	p1.setAge(12);
	p2.setAge(13);
	p1.setDescription("this is p1");
	p2.setDescription("this is p2");
	
	Person[] ps = new Person[2];
	ps[0] = p1;
	ps[1] = p2;
	
	ArrayList pl = new ArrayList();
	pl.add(0, p1);
	pl.add(1, p2);
	
	Map pm = new HashMap();
	pm.put("one", p1);
	pm.put("two", p2);
	
	request.setAttribute("p1", p1);
	request.setAttribute("p2", p2);
	request.setAttribute("ps", ps);
	request.setAttribute("pl", pl);
	request.setAttribute("pm", pm);
%>
<p>直接访问:p1.name=${p1.name }</p>
<p>访问数组:p1.name=${ps[0].name }</p>
<p>访问list:p1.name=${pl[0].name }</p>
<p>访问map:p1.name=${pm.one.name }</p>
<p>p2.name=${pm["two"].name }</p>
</body>
</html>
```

需要注意，EL表达式获取数据是使用`pageContext.findAttribute()` 方法，是在四个域对象当中寻找的，如果数据想要能够被EL表达式获取到就需要存储到四个域对象当中

### EL当中的运算符

关系运算符和逻辑运算符还有二元运算符都可以使用

同时还有empty运算符用于判断对象是否为null

用法为`${empty(ObjectName)}`

###  隐式对象

在EL当中有11个隐式对象

| 序号 |   隐含对象名称   | 描述                                                         |
| ---- | :--------------: | ------------------------------------------------------------ |
| 1    |   pageContext    | 对应于JSP页面中的pageContext对象（注意：取的是pageContext对象。） |
| 2    |    pageScope     | 代表page域中用于保存属性的Map对象                            |
| 3    |   requestScope   | 代表request域中用于保存属性的Map对象                         |
| 4    |   sessionScope   | 代表session域中用于保存属性的Map对象                         |
| 5    | applicationScope | 代表application域中用于保存属性的Map对象                     |
| 6    |      param       | 表示一个保存了所有请求参数的Map对象                          |
| 7    |   paramValues    | 表示一个保存了所有请求参数的Map对象，它对于某个请求参数，返回的是一个string[] |
| 8    |      header      | 表示一个保存了所有http请求头字段的Map对象，注意：如果头里面有“-” ，例Accept-Encoding，则要header[“Accept-Encoding”] |
| 9    |   headerValues   | 表示一个保存了所有http请求头字段的Map对象，它对于某个请求参数，返回的是一个string[]数组。注意：如果头里面有“-” ，例Accept-Encoding，则要headerValues[“Accept-Encoding”] |
| 10   |      cookie      | 表示一个保存了所有cookie的Map对象                            |
| 11   |    initParam     | 表示一个保存了所有web应用初始化参数的map对象                 |

