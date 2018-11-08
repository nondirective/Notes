# Runtime type identify and Reflect

>通常进行对象向上塑形可能会造成类型信息的丢失，还有其他特殊情况如仅有字节码对象的时候。

### 加载Class对象到虚拟机:

`Class Class.forName(String className);`
使用该方法能够得到一个Class对象返回值

假如已经加载到了虚拟机可用`objectName.getClass();`或者`className.class`获取

### 使用instanceof运算符鉴定对象

```
Person p = new Person;
p instanceof Person;  //True or false
```

### 使用Class对象的isInstance(Object o)方法鉴定对象
`Person.class.isInstance(p);  //True or false`

### 使用Class对象创建新实例
> 可以说Class对象就是.class对象具备类的所有信息,比如创建新实例也是可以做到的

`Person.Class.newInstance（）；`

---

## Reflect:
在运行期间Java是有了解Java类对象信息的能力的
使用前需要`import java.lang.reflect`包

获取类以及基类的方法信息：
```
Method[] ms = Person.class.getMethods();
for(Method m:ms){
  System.out.println(m);
}
```

获取类以及基类的字段信息：
```
Field[] f = Person.class.getFields();
for(Field value:f){
  System.out.println(value);
}
```
获取类以及基类的方法信息：
```
Constructor[] cs = Person.class.getConstractors();
for(Constructor c:cs){
  System.out.println(c);
}
```
