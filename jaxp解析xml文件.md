# Jaxp解析xml文件

按照解析的类型可以把jaxp解析分成两种方式....

- 层级结构解析->Dom
- 事件驱动解析->Sax

## Dom方式解析

使用这种方式解析是讲xml文件的整个结构加载到内存当中的，把标签，属性，内容都包装成对象

### 基本实现

```java
import javax.xml.parsers.*;

public class dom_parser{
    public static void main(String[] args) throws Exception {
        DocumentBuiderFactory dbf = DocumentBuiderFactory.newInstance();
        DocumentBuider db = dbf.newDocumentBuider();
        Document d = db.parse(/src/test.xml);
        NodeList nl = d.getElementByTagName("name");
        for(int i = 0;i<nl.getLength();i++){
            System.out.println(nl.item(i).getTextContent())
        }
    }
}
```





## Sax方式解析

时间驱动型，一边读取xml文档，同时做解析工作



## 文档回写