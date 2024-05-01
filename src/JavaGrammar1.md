# <center>java1</center>
## 基本类型
整数 二进制0b 八进制0 十六进制0x
```java
byte num1=2;// 8位，有符号 1个字节 1 2 4 8 16 32 64 1/0
short num2=4;// 16位，2个字节 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768
int num3=15;//32位，4字节
long num4=150L;//64位，8字节，L理论上不区分大小写，为防止l和1弄混，所以大写  
```
浮点数 少用浮点数进行比较，实在要用->BigDecimal类 
```java
float num5=15.0F;//32位，4字节，f 理论上不区分大小写，为和L统一，一并大写为好
double num6=15.00;//64位，8字节，可写可不写D/d，d 理论上不区分大小写，若要写，大写为好
```
字符
```java
char name='A';//除了可以表示标准的ASCIll，还表示一个16位的Unicode字符，2字节
String name2="hello";
```
布尔
```java
boolean flag=true;//boolean类型经过编译之后采用int来定义(所以此时boolean占4字节，32bits) 但如果是boolean数组则占1字节(8 bits)
```
> **查看类方法，ctrl+点击**  

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
### 类变量 vs 实例变量
类变量也叫静态变量，也就是在变量前加了static 的变量；实例变量也叫对象变量，即没加static 的变量；

区别在于：**类变量**是所有对象**共有**，其中一个对象将它值改变，其他对象得到的就是改变后的结果；而**实例变量**则属**对象私有**，某一个对象将其值改变，不影响其他对象；
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
选定方法名，alt+enter再选择add java doc就可以快速添加属性注释
```
## Scanner
```java
Scanner s=new Scanner(System.in);

scanner.close();//用完就关掉
```
通过Scanner类的`next()`与`nextLine()`方法获取输入的字符串,
类似c语言scanf(getchar()读缓冲区)和gets  
```java
next()：
一定要读取到有效字符后才可以结束输入。
对输入有效字符之前遇到的空白，next（）方法会自动将其去掉。
只有输入有效子符后才将其后面输入的空白作为分隔符或者结束符。
next()不能得到带有空格的字符串。

nextLine()：
1、以Enter为结束符也就是说nextLine（）方法返回的是输入回车之前的所有字符。
2、可以获得空白。
```
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
javac xxx.java //编译成字节码（class）文件
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

重写时有以下修饰符无法重写：
1. static 属于类，它不属于实例
2. final 常量
3. private

## 多态
多态注意事项：  
1. 多态是方法的多态，属性没有多态
2. 父类和子类，有联系，类型转换异常！
3. 存在条件：继承关系 方法需要重写，父类引用指向子类 Father f1=new Son()  

对象的多态：
1. 一个对象的编译类型和运行类型可以不一致
2. 编译类型在定义对象时，就确定了，不能改变
3. 运行类型是可以变化的
4. 编译类型看定义 = 号的左边，运行类型看 = 号的右边

方法的调用看运行类型。属性的调用看编译类型，或者说哪里声明哪里使用



## 抽象类
1. abstract可以用来修饰方法也可以用来修饰类，如果修饰方法，该方法就是抽象方法，如果修饰类，该类就是抽象类
2. 抽象类不能使用new，它是用来继承的，本质是类，只能单继承。
3. **抽象方法，只有方法的声明**，没有方法的实现，子类自己去实现，类似一个**模板**  
4. 子类继承抽象类，就必须要实现抽象类没有实现的抽象方法，否则该子类也要声明为抽象类
5. 抽象类可以有正常方法
## 接口
声明接口的关键字是**interface**，比抽象类更进一步，无法实现方法，仅有方法声明。**默认为public abstract修饰**  
jdk7.0前接口里的所有方法都没有方法体，jdk8.0后**接口类可以有静态方法，默认方法，也就是接口中可以有方法的具体实现**  
接口的修饰符只能是public和默认  
```java
interface 接口名 {
    属性//默认public static final修饰
    方法

    static void a() { //静态方法可以有方法体

    }

    default void b() {//默认方法可以有方法体

    }
}
```


## 匿名内部类
匿名内部类是定义在外部类的局部位置，比如方法中，并且没有类名  
主要是为了简化代码，对于那些只写一次，但每次不同的实现很有帮助  
> [匿名内部类详解](https://www.bilibili.com/video/BV1fh411y7R8?p=416&vd_source=17542f416e2251679b4c28b8e3f5e220)
```java
new 类/接口(参数列表){
    //....
};
//最简单的使用示例
new Test().say();
//也可用对象接受，此后反复调用
Test test = new Test(){
    @Override
    public void say() {
        System.out.println("lalala");
    }
};
class Test{
    public void say(){
        System.out.println("hhhh");
    }
}
```
在实现接口的匿名内部类(xxx)，实际上底层类似于
```java
class xxx implements 接口名{
    
}
```
而对于类的匿名内部类，实际上底层类似于
```java
class xxx extends 类名{
    
}
```

## 异常
> idea快捷键ctrl+alt+t选择代码块包裹

Throwable>Exception=Error  
Error: Java 虚拟机 无法解决的严重问题。如： JVM 系统内部错误、资源耗尽等。举例： 栈溢出 （StackOverflowError）  
Exception: 其它因编程错误或偶然的外在因素导致的一般性问题，可以使用针对性的代码进行处理。分为两大类：  
运行时异常： 又叫非受检异常，即程序运行时，发生的异常；  
编译时异常： 又叫受检异常，即编程时，编译器检查出的异常。
```java
try{
    //操作
}catch(异常类型 异常信息变量){
    //如果出现对应异常,执行
}finally{
    //善后工作，无论如何都会执行
}
printStackTrace()//打印错误栈信息
```
throw:主动抛出异常，一般在方法中使用,代码示例如下  
`throw new ArithmeticException(); `  
throws是声明一个异常，写在方法声明中,可以跟多个异常类名，用逗号隔开,方法不处理该异常，由调用该方法的上层语句处理  
`public void add() throws ArithmeticException,Exception`