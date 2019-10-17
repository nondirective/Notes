# MyBatis

## HelloWorld

添加mybatis3 的依赖

```xml
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis</artifactId>
	<version>3.5.2</version>
</dependency>
```

创建mybatis-config.xml文件，设置mybatis参数

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
          
        <!--数据库连接参数-->  
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/spring_jdbc?useSSL=true&amp;serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=UTF8"/>
        <property name="username" value="root"/>
        <property name="password" value="hhh123"/>
          
          
          
      </dataSource>
    </environment>
  </environments>
  <mappers>
    <!--映射的SQL语句所在的xml文档-->
    <mapper resource="mybatis-mapper.xml"/>
  </mappers>
</configuration>
```

映射SQL语句到mybatis-mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace属性的作用是设置命名空间在映射SQL语句重名时通过命名空间准确的定位到需要的映射的SQL语句-->
<mapper namespace="com.nond.mybatis_demo">
  <!-- id:访问映射语句的id,resultType:查询语句的返回bean类型-->
  <select id="selectPerson" resultType="com.nond.mybatis_demo.Person">
      <!--SQL 语句,#{id}为占位符，在程序中给定参数-->
    select * from person where id = #{id}
  </select>
</mapper>
```

获取SqlSession并使用其进行数据库查询

```java
@Test
public void test01() throws IOException {
    //获得配置文件输入流
	String mybatis_config_path = "mybatis-config.xml";
	InputStream is = Resources.getResourceAsStream(mybatis_config_path);
    
    //sqlSession工厂，通过它来获取到sqlsession
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
    
    //获取session并执行查询操作
	SqlSession sqlSession = sessionFactory.openSession();
    //参数statement就是前边在mybatis-mapper.xml文件当中设置的namespace+id
    //后面为参数列表
	Person p = sqlSession.selectOne("com.nond.mybatis_demo.selectPerson", 1);
	System.out.println(p);
}
```



