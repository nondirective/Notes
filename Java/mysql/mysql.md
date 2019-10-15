#  MySQL



## Redis和MySQL的区别

Redis是非关系性数据库同时也是缓存数据库（数据保存在缓存中，读取速度快但保存时间有限）

MySQL是持久化存储的关系型数据库（该类型的数据库访问都存在I/O操作相对比缓存数据库效率较低）

两者因为需求的不同一般配合使用（请求现在浏览器缓存中查找，没有再到数据库中查找）



DB(Database)数据库：存储数据的容器

DBMS(Database Management System)数据库管理系统：操作数据库的系统

SQL(Structure Query Language)结构化查询语言：控制数据库操作的命令语言



## 常用命令

本地登录

`mysql -u [userName] -p [password]`

云端登录

`mysql -h [hostName] -P hostPort] -u [userName] -p [password]`

查看所有的数据库

`show databases;`

使用数据库

`use [databaseName];`

查看当前使用的数据库

`select database();`

创建数据库

`create database [databaseName];`

删除数据库

`drop database [databaseName];`

库外查看所有表

`show tables from [databaseName];`

库内查看所有表

`show tables;`

查看表结构

`desc [tableName];`

查看数据库服务端版本

​	mysql内

​		`select version();`

​	cmd内	

​		`mysql --version`

​		`mysql -V`

创建表

```sql
create table tableName(
	columnName1 columnType1,
    columnName2 columnType2, 
);
```

注释

```sql
单行注释
	-- Content

多行注释
	/*
		Content
	*/
```

执行外部sql命令文件

`source [filePath];`



## DQL

### 基础查询

#### 着重号

着重号  **`**  ，在命令中遇到列明和关键字冲突时，列明用着重号包围说明是列名

>   select \`columnName\` from tableName;



#### 简单查询语句

```sql
-- 列出表中所有行的所有列
SELECT * FROM employees;
-- 列出表中所有行的`last_name`列和`salary`列
SELECT `last_name`,`salary` FROM employees;
```



#### 给查询列明指定别名

```sql
-- 方式一
SELECT `first_name` AS  "姓",`last_name` AS "名" FROM employees;
-- 方式二
SELECT `first_name` "姓",`last_name` "名" FROM `employees`;
```



#### 去除查询结果中值重复的项

首先我们需要知道**DISTINCT**关键字只能够放在查询字段的开头，一般而言DISTINCT用来查询不重复记录的条数

```sql
-- 查询单值去重
SELECT DISTINCT `job_id` FROM `employees`;
```

需要注意，如

>   SELECT DISTINCT \`department_id\`,\`job_id\`  FROM employees;

这条查询语句的意义过滤两个字段重复的记录，并不是想象中的查询department_id各不相同的job_id



#### SQL中加号的作用

对与两个数值型字段相加，进行加法运算

相加两方存在字符型，试图将字符型字段转换成数值型再做加法运算

相加两方当中有一方为**NULL**则结果一定为NULL



如果需要进行连接字符串操作，可以使用**CONCAT**函数



#### 连接字符串

使用到`CONCAT(str1,str2,...)`函数，将入参字符串按顺序拼接

```sql
-- 连接姓和名
SELECT CONCAT(`first_name`," ",`last_name`) FROM `employees`;
```

### 模糊查询

首先提到一点，如果模糊查询条件中有单个特殊字符需要转义的可以使用到   \  进行单个特殊字符的转义 

如果在模糊查询条件中所有的特殊字符都需要转义，这时就可以使用**ESCAPE**关键字指定需要转移的字符

如

```sql
SELECT `last_name` FROM `employees` WHERE `last_name` LIKE '$_$' ESCAPE '$';
```



#### LIKE

**LIKE**一般搭配通配符使用

|通配符|说明|
|---|---|
| %| 包含零个或多个字符的任意字符串|
| _ | 任何单个字符。                                         |
| [ ]         | 指定范围 ([a-f]) 或集合([abcdef]) 中的任何单个字符，如`[a-b]` |
| [^]         | 不属于指定范围 ([a-f]) 或集合([abcdef]) 的任何单个字符，如`[^a-b]` |



#### BETWEEN  AND

```sql
-- BETWEEN AND语句等价于第二个查询语句
-- 1. 使用 BETWEEN AND
SELECT salary FROM employees WHERE salary BETWEEN 5000 AND 10000;
-- 2. 使用><= AND
SELECT salary FROM employees WHERE salary>=5000 AND salary<=10000;
```

#### IN

限制条件值在一个数值列表内

如

```sql
SELECT `last_name` FROM employees WHERE `manager_id` IN("108","120");
```



#### IS NULL

普通的=运算符不能够用于判断null值

判断是否为null,可以使用IS NULL关键字

这条语句的作用仅用于判断null值



#### 安全等于

安全等于：<=>

这个等于可以用来判断数值是否相等也可以用来判断数值是否为null



### 排序查询

如果需要对查询结果进行排序，用ORDER BY [columnName]根据列名进行结果排序

```sql
-- ASC升序
SELECT `last_name`,`salary` FROM employees ORDER BY salary ASC;
-- DESC降序
SELECT `last_name`,`salary` FROM employees ORDER BY salary DESC;
-- 先按salary升序排序再按employee_sid降序排序
SELECT `employee_id`,`salary` FROM employees ORDER BY salary ASC,`employee_id` DESC;
-- 可以使用别名
SELECT `employee_id` AS 编号,`salary` AS 工资 FROM employees ORDER BY 工资 DESC;
```



### 分组查询

使用分组函数查询的结果可以进行分组

如，查询每个部门员工的工资总和

```sql
SELECT
	SUM(salary),department_id 
FROM
	employees 
WHERE 
	department_id IS NOT NULL 
GROUP BY 
	department_id;

/*结果
4400.00	    10
19000.00	20
24900.00	30
6500.00	    40
156400.00	50
28800.00	60
10000.00	70
304500.00	80
58000.00	90
51600.00	100
20300.00	110
*/
```



如果需要对已经分组的查询结果进行条件限制，此时不能使用WHERE关键字，而是使用到HAVING关键字

如在上述案例的要求上再加一个部门工资总和需要超过10000的条件则查询语句变为

```sql
SELECT
	SUM(salary),department_id 
FROM
	employees 
WHERE 
	department_id IS NOT NULL 
GROUP BY 
	department_id
HAVING
	SUM(salary)>=10000;
```



此外，分组查询还可以根据多个字段分组

### 连接查询

综合多表数据进行查询，这里使用到SQL99的连接查询标准

#### 内连接

##### 等值连接

根据表间的关联字段是否相同进行连接查询

如，查询员工名字和部门名称，其中员工名字在employees表、部门名称在departments表，通过department_id字段关联

```mysql
SELECT e.`last_name`,d.`department_name`
FROM employees e
INNER JOIN departments d
ON e.`department_id`=d.`department_id`;
```

##### 非等值连接

非等值连接用到BETWEEN AND或LIKE等等不等值作为连接条件

如，查询员工工资的工资等级，员工工资在employees表，等级在job_grade表，使用job_grade的lowest_sal和highest_sal字段进行连接

```mysql 
SELECT e.salary,g.grade_level
FROM job_grades AS g
INNER JOIN employees AS e
ON e.salary BETWEEN g.`lowest_sal` AND g.`highest_sal`
ORDER BY g.`grade_level` ASC;
```

##### 自连接

根据表中的某一字段连接相同表中另一字段，如employees表中的employee_id-->manager_id-->employee_id

即查询员工的领导，领导同时也是员工这时候使用到自连接



#### 外连接

##### 左、右外连接

以一方为主表，一方为从表，主表的记录被全部查询出来，然后再根据连接条件得到相应的从表记录，如果没有查询到相匹配的从表记录则值为null

以左外连接为例，即以左表为主表

```mysql
SELECT e.last_name,d.department_name
FROM employees e
LEFT OUTER JOIN departments d
ON e.`department_id`=d.`department_id`;
```

同理，右外连接的关键字为RIGHT OUTER JOIN，以右表为主表

#### 交叉连接

即查询两表结果的笛卡尔积

```sql
SELECT e.last_name,d.department_name
FROM employees e
CROSS JOIN departments d;
```

### 子查询

 指在一条select语句中，嵌入了另外一条select语句，那么被嵌入的select语句称之为子查询语句

#### 子查询分类

按子查询出现的位置：

*   SELECT后

    仅支持标量子查询

*   FROM后

    支持表子查询

*   WHERE或HAVING后

    标量子查询

    列子查询

    行子查询

*   EXISTS后（相关子查询）

    表子查询

    

按结果集的行列数不同：

*   标量子查询（结果集只有一行一列）
*   列子查询（结果集只有一列多行）
*   行子查询（结果集只有一行多列）
*   表子查询（结果集为多行多列）



#### WHERE后或HAVING后

##### 标量子查询

结果集只有一行一列，

```mysql
-- 找出工资和112号员工相同的员工的名字

SELECT last_name
FROM employees
WHERE salary=(
	SELECT salary 
	FROM employees
	WHERE employee_id=112
);
```

##### 列子查询

结果集为一列多行，与子查询连接的运算符有IN、ANY（SOME）、ALL

```mysql
-- 查找IT部中工资比管理部任意一个人工资高的员工的名字

SELECT last_name
FROM employees
WHERE salary>ANY(
	SELECT salary
	FROM employees
	INNER JOIN departments
	ON employees.department_id=departments.department_id
) AND department_id=ANY(
	SELECT department_id
	FROM departments
	WHERE department_name='IT'
);
```

##### 行子查询

略

```mysql
SELECT *
FROM employees
WHERE (salary,employee_id)=(
	SELECT MAX(salary),MIN(employee_id)
	FROM employees
);
```



#### SELECT后

```mysql
-- 部门信息以及统计每个部门的人数
SELECT d.*,(
	SELECT COUNT(*)
	FROM employees e
	WHERE d.department_id=e.department_id
)
FROM departments d;
```

#### FROM后

```mysql
-- 查询每个部门平均工资的工资等级
SELECT avg_department.d_id,g.grade_level
FROM (
	SELECT AVG(salary) avg_salary,department_id d_id
	FROM employees e
	GROUP BY e.department_id
)avg_department 
JOIN job_grades g
ON avg_department.avg_salary BETWEEN g.lowest_sal AND g.highest_sal

```

#### EXISTS中

```mysql
-- 查询有员工的部门名
-- 使用EXISTS函数，如果EXISTS中有结果则返回1，无则返回0
SELECT d.department_name
FROM departments d
WHERE EXISTS(
	SELECT *
	FROM employees e
	WHERE e.department_id=d.department_id
);
```

### 分页查询

使用到mysql的limit方言，作用是对查询结果进行分页，

​	参数start：起始索引，最低为0

​	参数size：结果长度，当前分页的结果条数

案例：查询工资排名11-20的员工信息

```mysql
SELECT *
FROM employees
ORDER BY salary DESC
LIMIT 10,10;
```

### 联合查询

在查询需要得到来自多表的结果时需要用到联合查询，如中国用户表和外国用户表需要合并成一个表时

需要注意，两个表查询的列数需要一致，结果集的列名为第一个查询的列名

并且联合之后结果集会自动去重



```mysql
CREATE TABLE p_cn(
	id INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(20)
	);
	
CREATE TABLE p_an(
	id INT PRIMARY KEY AUTO_INCREMENT,
	`an_name` VARCHAR(20)
	);
	
INSERT INTO p_cn(`name`) VALUES('小明');
INSERT INTO p_an(`an_name`) VALUES('hari');

SELECT `name`,id fromp_cn 
UNION 
SELECT `an_name`,id FROM p_an;
```





## DML

### 插入数据

插入数据到表有两种方式

#### 方式一

该方式同时多行插入，支持子查询

```mysql
INSERT INTO person(name,age) values('hari',11);

-- 同时多行插入
INSERT INTO person(name,age) values('hari',11),('mari',11);

-- 对子查询的支持，返回结果为结果集省略values
INSERT INTO person(name,age) 
SELECT 'hari',12;
```



#### 方式二

不支持多行插入也不支持子查询

 ```mysql
INSERT INTO person
SET `name`='hari',age=11;
 ```

### 更新数据
#### 单表更新

格式 ：

​		UPDATE 表名 SET 列名=值,..... WHERE 限制条件



#### 多表更新

多表更新使用到表的连接，两表组合成一个表再进行修改

```mysql
UPDATE employees e
LEFT OUTER JOIN departments d
ON e.department_id=d.department_id
SET d.department_name='IT',e.salary=5000`employees`
WHERE e.employee_id=206;
```

### 删除数据

#### 使用DELETE

特点：

*   删除数据可回滚

*   可以针对单行删除

*   可以多表连接删除

*   AUTO_INCREMENT自增值从断点开始

*   删除有返回值

格式：`DELETE FROM 表名 [WHERE 限制条件] `



#### 使用TRUNCATE

特点：

*   删除数据不可回滚
*   删除仅针对整个表
*   不支持多表连接删除
*   AUTO_INCREMENT自增值从新开始
*   删除无返回值

格式：`TRUNCATE TABLE 表名`

## DDL

### 库操作

#### 创建库

创建库:`CREATE DATABASE 库名`

如果需要避免库已经存在的错误的话可以给语句加上判断语句

`CREATE DATABASE IF NOT EXISTS 库名`

#### 删除库

`DROP DATABASE 库名`

避免库不存在的错误

`DROP DATABASE IF EXISTS 库名`

#### 查看库字符集

`SHOW CREATE DATABASE 库名`

```mysql
SHOW CREATE DATABASE myemployees;

/*
Database	Create Database
myemployees	CREATE DATABASE `myemployees` /*!40100 DEFAULT CHARACTER SET gbk */ /*!80016 DEFAULT ENCRYPTION='N' */
*/
```

#### 修改表字符集

`ALTER DATEBASE 库名 CHARACTER SET 字符集`

#### 修改数据库名

当前版本语句修改库名已经不可行了，会对库造成影响

可行的方法是直接修改库本地文件系统下的文件夹名

### 表操作

#### 创建表

语法：

```mysql
CREATE TABLE 表名(
	列名 列类型(长度) 约束,
    列名 列类型(长度) 约束,
    ...
    列名 列类型(长度) 约束
)
```

可添加IF NOT EXISTS判断



#### 修改表

语法：

```mysql
ALTER TABLE 表名 MODIFY|CHANGE|ADD|DROP COLUMN 列名 [列类型 约束]
```



##### 修改列类型、约束（MODIFY）

语法：`ALTER TABLE 表名 MODIFY COLUMN  列名 新类型 新约束` 



##### 修改列名、列类型、约束（CHANGE）

语法：`ALTER TABLE 表名 CHANGE COLUMN 旧列名 新列名 列类型 约束`

如果通过MODITY仅修改列名还要提供原本的列类型和约束，如果不提供列类型，语句将无法执行，如果不提供原本的约束，原本的约束将丢失

也即是说通过这种方法修改，需要提供列名、列类型、约束着三个信息



##### 添加列

语法：`ALTER TABLE 表名 ADD COLUMN 列名 列类型 约束`



##### 删除列

语法：`ALTER TABLE 表名 DROP COLUMN 列名`



##### 表重命名

`ALTER TABLE 旧表名 RENAME TO 新表名`



##### 删除表

语法：`DORP TABLE 表名`

可添加IF EXISTS判断



##### 复制表

可以对表进行多种赋值操作

*   仅复制表结构
*   仅复制表的部分结构（子查询）
*   复制表的结构以及记录（子查询）
*   复制表的部分结构以及部分记录（子查询）

###### 仅复制表结构

语法：

```mysql
CREATE TABLE 新表名 LIKE 旧表名;
```



###### 仅复制表的部分结构

语法：

```mysql
CREATE TABLE 新表名
SELECT 列名1，列名2
FROM 旧表名
WHERE 矛盾表达式(如1=0)
```



###### 复制表的结构以及记录

语法：如上

```mysql
CREATE TABLE 新表名
SELECT * 
FROM 旧表名
```



###### 复制表的部分结构以及部分列记录

略，参考仅复制表的部分结构，将矛盾表达式更换成想要的限制条件即可



## 字符函数

#### length

返回传入字符串的自付出，mysql的utf8字符集中每个中文大小为3

#### concat

拼接字符串，上文已经提到

#### upper

将传入字符串转换大写

#### lower

将传入字符串装换成小写

#### substr

需要注意sql中索引不是从0开始而是从1开始

截取字符串，这里提到该函数的两个重载

```sql
/*
重载一，substr(str,index)
从index开始往后截取字符串至字符串结尾
*/
SELECT SUBSTR('abcde',3);
/*
重载二，substr(str,index,length)
从index开始往后截取长度为length的字符串
*/
SELECT SUBSTR("abcde",3,2);
```

#### instr

即Java中的String.indexOf(str)

在sql中为INSTR(str,subStr)

```sql
SELECT INSTR('abcde','cde');
```

#### trim

TRIM([{BOTH | LEADING | TRAILING}] [remstr] FROM str)  

对字符首尾的字符进行过滤 ，如‘    abc     ’，trim就可以吧首尾的空格给过滤掉

BOTH、LEADING、TRAILING为可选项，分别对应对前后、前、后字符进行过滤

remstr为需要过滤的字符，不指定时默认为空格

```sql
SELECT TRIM(BOTH ' ' FROM '     abc      ');
```

#### lpad

对不满足指定长度的字符串进行左填充

LPAD(str,len,padstr)

padstr为填充字符

如果str的长度也超过了len，str也将会被截取到长度等于len为止

#### rpad

对不满足指定长度的字符串进行左填充

RPAD(str,len,padstr)

padstr为填充字符

如果str的长度也超过了len，str也将会被截取到长度等于len为止

#### replace

REPLACE(str,tarstr,repstr)

替换字符串的某一段

## 数学函数



#### round

对数值进行四舍五入，在未提供bit时默认四舍五入到个位，提供了bit时四舍五入到bit位小数

round(value,[bit])

#### ceil

CEIL(value)，该函数返回一个大于value值的最小整数

#### floor

FLOOR(value)，该函数返回一个小于value值的最大整数

#### truncate

truncate(value,bit)，按bit截断小数部分，即截断到bit位小数

## 日期函数

#### now

返回当前系统日期+时间

#### curdate

返回当前系统日期

#### curtime

返回当前系统时间

#### str_to_date

STR_TO_DATE(str,format)

根据提供的格式将字符串转换成data类型值

```sql
/*	
	%y  两位年份
	%Y  四位年份
	%m  月，小于十月则十位用零填充
	%c  月
	%d  日
	%H  24小时制小时
	%h  12小时制小时
	%i  分钟
	%s  秒
*/

SELECT STR_TO_DATE('2019-09-19,18:00:00','%Y-%m-%d,%H:%i:%s');
```

#### date_format

DATE_FORMAT(date,format)

将日期按模板返回字符串

```sql
SELECT DATE_FORMAT(CURDATE(),"%y-%c-%d");

-- 19-10-13
```



## 流程控制函数

#### if

IF(exp,T_return,F_return)

#### case

类似于swich，但用法不同，在sql中还有两种格式

```sql
-- 判断case后时候于when后相同
SELECT CASE 1
WHEN 1 THEN 1
WHEN 0 THEN 0
ELSE -1
END;

-- 判断when后表达式是否成立
SELECT CASE
WHEN 1=2 THEN 1
WHEN 1=0 THEN 0
ELSE -1
END;
```

## 分组函数

分组函数默认情况都是忽略null值的

#### sum

对查询字段求和

#### avg

对查询字段求平均数

#### min

对查询字段求最小值

#### max

对查询字段求最大值

#### count

对查询字段计算出现次数





上述的五个函数都能够搭配DISTINCT使用进行去重后计算



查询表中记录数

一般使用COUNT(*)

