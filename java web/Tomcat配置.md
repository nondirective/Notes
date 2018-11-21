

# Tomcat



## 配置Tomcat

### 安装

把当前目录下的`apache-tomcat-7.0.42.zip` 压缩包解压就当做是安装完成了

### 启动tomcat服务器

```bash
$ cd apache-tomcat-7.0.42/bin
$ sh start.sh
```

### 修改端口

打开conf文件夹下的server.xml文件

找到`<service name="Catalina">` 下的`<Connector port="8080">` 修改port属性 

### 创建eclipse的java web项目



![创建项目](/home/nondirectional/workspace/Notes/java web/创建项目1.png)

项目资源放入WebContent

