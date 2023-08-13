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





