## Object
### getclass
```java
对象名.getclass();//返回此Object的运行时类型
```

### toString
```java
public String toString();//对象的字符串表示形式。
System.out.println(a);//==a.toString()
```
当直接输出一个对象时，toString方法会默认调用  
子类一般会重写toString方法

### hashcode
```java
hashcode();//返回对象的哈希值
```
1. 提高具有哈希结构的容器效率
2. 两个引用，如果指向的是同一个对象，则哈希值肯定一样
3. 两个引用，如果指向的是不同对象，则哈希值不一样
4. 哈希值主要根据地址得来，但并不等同地址，因为java是在虚拟机上运行的

### finalize
[finalize详解](https://www.bilibili.com/video/BV1fh411y7R8?p=327&vd_source=17542f416e2251679b4c28b8e3f5e220)
1. 当对象被回收时，系统自动调用该对象的finalize方法。子类可重写该方法
2. 什么时候被回收：当某个对象没有任何引用时，则jvm认为其为垃圾对象，使用垃圾回收机制来销毁该对象，在销毁该对象前，会先调用finalize方法
3. 垃圾回收机制的调用，是由系统来决定也可以通过System.gc()主动触发垃圾回收机制  

## String
|        方法        |           作用            |
|:----------------:|:-----------------------:|
|      equals      |      判断是否相等（区分大小写）      |
| equalsIgnoreCase |     判断是否相等（不区分大小写）      |
|      length      |          字符串长度          |
|     IndexOf      | 字符在字符串中第一次出现位置，找不到返回-1  |  
|   lastIndexOf    | 字符在字符串中最后一次出现位置，找不到返回-1 |  
|    substring     |        截取指定范围的子串        |  
|       trim       |          去前后空格          |  
|   toUpperCase    |         字符串转大写          |  
|   toLowerCase    |         字符串转小写          |
|      charAt      |        下标读取字符串字符        |
|      concat      |      拼接字符串(和+号比较)       |
|     replace      |        替换字符串的字符         |
|      split       |          分割字符串          |
|    compareTo     |        比较两字符串大小         |
|   toCharArray    |         转换为字符数组         |
|      format      |          格式字符串          |

## String Buffer
|     方法      |                作用                |
|:-----------:|:--------------------------------:|
|   append    |            向字符串末尾增加字符            |
|   delete    |   (start,end)删除[start,end)的字符    |
|   replace   | (start,end,"xxx")替换[start,end)字符 |
|   IndexOf   |      子串在字符串中第一次出现位置，找不到返回-1      |  
|   insert    |              指定位置插入              |  
|   length    |               获取长度               |  

