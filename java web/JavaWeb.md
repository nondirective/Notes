# JavaWeb攻略之路

## Tomcat服务器的配置

Tomcat的所有配置文件都在conf文件夹当中，而其中的server.xml文件是核心配置文件

### 服务器端口的配置

找到以下片段

```XML
<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
```

其中的`port = 8080`就是指定服务器的端口号

访问服务器就使用`http://localhost:端口号` 进行访问

### 服务器虚拟映射

#### 方式一
把本地的文件目录映射到服务器上，并且指定一个别名

假定有项目在`C:/`目录下，把`C:/`映射为`/JavaWebApps`，这样就能够使用`http://localhost:8080/JavaWebApps/资源名称`的方式来访问资源


在`server.xml` 文件当中找到
```XML
<Host name="localhost"  
appBase="webapps"
unpackWARs="true" 
autoDeploy="true">

<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
prefix="localhost_access_log." 
suffix=".txt"
pattern="%h %l %u %t &quot;%r&quot; %s %b" />
</Host>
```

设置一个虚拟映射 ，则在`<Host>`标签当中插入一个`<Context>`标签

```XML
<Context path="/JavaWebApps" docBase="C:\Users\Administrator\">
```

　其中，Context表示上下文，代表的就是一个JavaWeb应用，Context元素有两个属性，

1. **path**：用来配置虚似目录，必须以`/`开头
2. **docBase**：配置此虚似目录对应着硬盘上的Web应用所在目录

![1546883134575](C:\Users\Administrator\Desktop\1546883134575.png)

#### 方式二
这种方式为Tomcat的默认映射方式

只需要把项目目录放在Tomcat客户端目录的`/webapps`目录下，然后使用`http://localhost:8080/资源文件`访问即可

## 对Http协议响应头使用的小例子

### 1.重定向

```java
package com.nondirectional;

import javax.servlet.*;
@WebServlet("/Servlet01")
public class Servlet01 extends HttpServlet{
    protected void doGet(HttpServletRequest req,HttpServletResponse resp){
        resp.setStatus(302)  //302为重定向状态码
        resp.setHeader("Location","http://www.baidu.com")
    }
    
    protected void doPost(HttpServletRequest req,HttpServletResponse resp){
        this.doGet(req,resp);
    }

}
```

### 2.告知浏览器数据压缩格式 

```java
package com.nondirectional;

import javax.servlet.*;
import java.io.*;

public class Main extends HttpServlet{
    protected void doGet(HttpServletRequest req,HttpServletResponse resp){
        String data = "You eyes shine like star in the sky.";
        ByteArraysOutputStream bout = new ByteArraysOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(bout);  //buffer
        resp.setHeader("Content-length",new Integer(data.getBytes().length).toString());  //原始数据的大小
        gout.write(data.getBytes());
        gout.close(); 
        resp.getOutputStream().write(bout.toByteArray());
        resp.setHeader("Content-Encoding","gzip");
        bout.close();
    }
}
```

### 3.定时跳转到指定页面																																																																																																																																		

```java
package com.nondirectional;

import javax.servlet.*;

public class Main extends HttpServlet{
	protected void doGet(HttpServletRequest req,HttpServletResponse resp){
		resp.setHeader("refresh","3;http://www.baidu.com");
        //三秒钟之后跳转到百度网
    }
}
```

