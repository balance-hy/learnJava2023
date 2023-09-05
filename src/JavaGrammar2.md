# <center>java2</center>
## enum 枚举
### 声明枚举
声明枚举时必须使用 enum 关键字，然后定义枚举的名称、可访问性、基础类型和成员等。枚举声明的语法如下：  
```java
enum-modifiers enum enumname:enum-base {
    enum-body,
}
//修饰符 enum 名字:类型{
//}
```  
提示：如果没有显式地声明基础类型的枚举，那么意味着它所对应的基础类型是int。  
```java
enum SexEnum {
    male,female;
}
//调用
```  
之后便可以通过枚举类型名直接引用常量，如 SexEnum.male、SexEnum.female。  
### 枚举类
Java 中的每一个枚举都继承自 java.lang.Enum 类。当定义一个枚举类型时，每一个枚举类型成员都可以看作是 Enum 类的实例，这些枚举成员默认都被final、public、static 修饰，当使用枚举类型成员时，直接使用枚举名称调用成员即可。  
所有枚举实例都可以调用 Enum 类的方法
`SexEnum.values()[i]`

| 方法名称 | 描述 |
|:-------------|:--------------:|
| values() |   以数组形式返回枚举类型的所有成员  |
| valueOf()    |    将普通字符串转换为枚举实例    |
| compareTo()    |    比较两个枚举成员在定义时的顺序    |
| ordinal()   |    获取枚举成员的索引位置    |  
详见：[http://c.biancheng.net/view/1100.html](http://c.biancheng.net/view/1100.html)
## 基本注解
使用Annotation时前加@符号，将其当做一个修饰符使用，用于修饰它支持的程序  
@interface:代表是注解类 
### @Override
重写，只能作用某个方法
### @Deprecated
表示某个程序（类、方法等）过时
### @SuppressWarnings()
抑制编译器警告

## 包装类
八种基本数据类型的引用类型即为包装类  
这样有了类的特点，可以调用类的方法

| 基本数据类型  |   对应包装类   |
|:--------|:---------:|
| byte    |   Byte    |
| boolean |  Boolean  |
| short   |   Short   |
| char    | Character |  
| int     |  Integer  |  
| long    |   Long    |  
| float   |   Float   |  
| double  |  Double   |  
装箱：基本数据类型->对应包装类，拆箱反之。jdk5后自动装箱拆箱
```java
//手动装箱示例 int->Integer 其他包装类类似
int n=100;
Integer integer = new Integer(n);//法一
Integer integer1 = Integer.valueOf(n);//法二
//手动拆箱示例 Integer->int
int i=integer.intValue();

//jdk5后自动装箱拆箱
//自动装箱
int n2=200;
Integer integer2=n2;//实际上是调用了valueof方法
//自动拆箱
int n3=integer2;//实际上是调用了intValue方法

//包装类间转换 Integer->String
Integer i=1;
//方式一
String str=i+"";
//方式二
String s = i.toString();
//方式三
String s1 = String.valueOf(i);

//包装类间转换 String->Integer
String s2="12345";
//方式一
Integer i1 = Integer.parseInt(s2);
//方式二
Integer i2 = new Integer(s2);
```  
