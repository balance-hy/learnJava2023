# <center>java</center>
## 基本类型
整数 二进制0b 八进制0 十六进制0x
```java
byte num1=10;  
short num2=10;  
int num3=10;  
long num4=10L;//long类型要加L  
```  
浮点数 少用浮点数进行比较，实在要用->BigDecimal类 
```java
float num5=10.3F;//float要加F
double num6=10.34577;
```  
字符
```java
char name='A';//占2字节，c占一字节
String name1="hello";
```
布尔
```java
boolean flag=true;
```
**查看类方法，ctrl+点击**  

## 变量  

### 局部变量
方法中声明以及初始化
```java
public static void main(String[] args) {
        int i;
}
```
### 实例变量
类中方法外声明以及初始化,默认为该类型默认值
```java
public class Demo1 {
    int i;
    public static void main(String[] args) {
        Demo1 demo1=new Demo1();
        System.out.println(demo1.i);
    }
}
```

### 类变量
类中方法外且需加修饰符static，默认为该类型默认值
```java
public class Demo1 {
    static int i;
    public static void main(String[] args) {
        System.out.println(i);
    }
}
```

## 常量
`final`修饰,常量名大写(+下划线),仅可一次赋值
```java
public class Demo1 {
    static final int I=1;
    //等价于final static  int I;不区分先后顺序
    public static void main(String[] args) {
        System.out.println(I);
    }
}
```
## 文档注释
```java
/**
 * @author 作者
 * @version 版本
 * @since jdk版本至少为
 * @param 参数名
 * @return 返回值
 * @throws 抛出异常情况
 */
```
## Scanner
```java
Scanner s=new Scanner(System.in);

scanner.close();//用完就关掉
```
通过Scanner类的`next()`与`nextLine()`方法获取输入的字符串,
类似c语言scanf(getchar()读缓冲区)和gets  

`hasNext()`和`hasNextLine()`判断是否还有输入的数据
其他数据类型输入只需后加数据类型即可如`nextInt()`
## 增强for循环
```java
for(声明语句;表达式){
    //代码    
}

int nums[]={0,10,20,30,40}
for(int x;nums){
    System.out.println(x);    
}
```
## 方法重载
方法名相同，参数列表必须不同，返回类型可同可不同

## 命令行传参
有时候需要程序运行时传递信息，这要靠传递命令行参数给main方法实现  
执行时传递参数即可
```java
javac xxx.java
java  xxx.class 参数
```
## 可变参数
方法声明中在指定参数类型后加一个省略号...  
一个方法只能指定一个可变参数，它必须是方法的最后一个参数。任何普通参数都  
需在它之前声明。其实就是定义了一个可变长度指定类型的数组。  
## 数组
```java
int[] a=new int[10];
int b[]=new int[10];//一样,new类似于malloc

//获取数组长度
int len=a.length;
```
## java内存
堆：
1. 存放new的对象和数组
2. 所有线程共享，不存放对象引用  

栈：
1. 存放基本变量类型及其值
2. 引用对象的变量（存放这个引用在堆里的地址）  

方法区：
1. 所有线程共享
2. 存放所有class和static变量

## super
1. super调用父类的构造方法，必须在构造方法的第一个
2. super必须只能出现在子类的方法或者构造方法中
3. super和this不能同时调用构造方法  

### Vs this     
代表对象不同  
&emsp;&emsp;this:本身调用这个对象  
&emsp;&emsp;super:代表父类对象的应用 
前提  
&emsp;&emsp;this: 没有继承也可以使用      
&emsp;&emsp;super:继承时才可以使用  
构造方法  
&emsp;&emsp;this();本类的构造  
&emsp;&emsp;super();父类的构造  

## 重写
需要有继承关系，子类重写父类的方法。  
1. 方法名必须相同
2. 参数列表必须相同
3. 修饰符：范围可以扩大不可缩小 private<default<protected<public
4. 抛出的异常：范围可以缩小不可扩大
