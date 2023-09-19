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
> 枚举详见：[http://c.biancheng.net/view/1100.html](http://c.biancheng.net/view/1100.html)
## 基本注解
使用Annotation时前加@符号，将其当做一个修饰符使用，用于修饰它支持的程序  
### @interface
代表是注解类 
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
## StringBuffer
java.lang.StringBuffer代表**可变的字符序列**，可以对字符串内容进行增删。  
很多方法和String相同，但StringBuffer是可变长度的(继承了AbstractStringBuilder,该类有属性
char[] value,而数组是存放在堆中，所以可以修改)  

String VS StringBuffer
1. String保存字符串常量，里面的值不能修改，每次String的更改，实际上是更改地址，**效率低**
2. StringBuffer保存字符串变量，里面的可以修改，无需每次更新地址（只有修改导致空间不够才更新地址），**效率高**  

```java
//String -> StringBuffer
//方式一，通过构造器
String s="aaa";
StringBuffer stringBuffer = new StringBuffer(s);

//方式二 append
StringBuffer stringBuffer = new StringBuffer();
StringBuffer a_s=stringBuffer.append(s);

//StringBuffer -> String
//方式一 toString
StringBuffer stringBuffer = new StringBuffer("aaa");
String s=stringBuffer.toString();

//方式二
StringBuffer stringBuffer = new StringBuffer("aaa");
String s=new String(stringBuffer);
```
StringBuffer是一个容器

## StringBuilder
继承了AbstractStringBuilder  
一个可变的字符序列，提供一个与StringBuffer兼容的API，但不保证同步
即**不是线程安全**的，该类被设计用作StringBuffer的简易替换，用于
字符串缓冲区被**单线程使用时**，比StringBuffer**快**。  
在StringBuilder上的主要操作是append和insert。

## BigInteger
处理大整数

## BigDecimal
处理高精度数

## 日期类
### Date
精确到毫秒，SimpleDateFormat类用于格式和解析日期  
例如可以把格式（日期->文本）,解析（文本->日期）以及规范化
```java
//获取当前系统时间，默认格式为国外
Date date = new Date();
//格式化
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss E");
String format = simpleDateFormat.format(date);
//字符串转date
String s="1996年01月01日 10:20:30 星期一";
Date parse=simpleDateFormat.parse(s);
```
### Calendar
Calendar是抽象类，无法通过new来实例化，可以用它的getInstance方法获取实例  
该类提供了大量的方法和字段，但没有提供类似data的格式化类，因此需要自己去组合输出，比较灵活
```java
Calendar instance = Calendar.getInstance();
instance.get(Calendar.YEAR);//取年份，其他类似
System.out.println(instance.get(Calendar.YEAR)
        +"年"+instance.get(Calendar.MONTH)
        +"月"+instance.get(5)+"日" 
        +instance.get(Calendar.HOUR)+":"
        +instance.get(Calendar.MINUTE)+":"
        +instance.get(Calendar.SECOND));
//上面默认是12小时制，若要改为24小时制，将Calendar.HOUR改为Calendar.HOUR_OF_DAY即可
instance.get(Calendar.HOUR);
instance.get(Calendar.HOUR_OF_DAY);
//可以看到括号里还可以写数字，因为Calendar类中数字对应了属性
```
### 第三代日期
前面两代日期都存在问题，比如Calendar是可变的,但日期和时间应为不可变  
又比如Date中年份从1900开始，月份从0开始，Calendar也没有格式化的工具  
而且它们都不是线程安全的，也无法处理闰秒问题（每两天多出一秒）  
#### LocalDate
包含年月日
#### LocalTime
包含时分秒
#### LocalDateTime
包含年月日时分秒
```java
LocalDateTime now = LocalDateTime.now();//获得实例，其余两个类似
System.out.println("年"+now.getYear());//获得年份
System.out.println("年"+now.getMonth());//获得月份 英文 如MARCH
System.out.println("年"+now.getMonthValue());//获得月份 数字如3
```
#### DateTimeFormatter 格式日期类
和SimpleDateFormat类似  
```java
LocalDateTime now = LocalDateTime.now();
DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss E");
String date=dateTimeFormatter.format(now);
System.out.println(date);
//2023年09月10日 14:17:32 星期日
```
#### Instant 时间戳
类似于Date,提供了一系列和Date类转换的方式  
```java
//Instant->Date
Instant instant=Instant.now();//当前时间戳
Date date=Date.from(instant)
//Date->Instant
Instant instant=date.toInstant();
```
plus和minus方法用于知道加减时间后的对应时间。

## 集合
### 集合体系
1. 集合主要是两组（单列集合，双列集合）
2. Collection 接口有两个重要的子接口 List Set,它们的实现子类都是单列集合，也就是一个个对象(或者说元素)
3. Map接口的实现子类是双列集合，存放的KEY-VALUE键值对

![Collection](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309111504327.png)

![Map](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309111505273.png)

### Collection
collection实现子类可以存放多个元素，每个元素可以是Object  
有些Collection的实现类，可以存放重复元素，有些不可以  
有些Collection的实现类，是有序的（List），有些无序（Set）  
Collection接口没有直接的实现子类，是通过它的子接口List、Set来实现的
#### 遍历
只要是实现了Collection接口的类，可以用以下两种遍历方式  
##### Iterator 迭代器
Iterator对象称为迭代器，主要用于遍历Collection集合中的元素  
所有实现了Collection接口的集合类都有一个iterator方法，用以返回一个实现了Iterator接口的对象，即迭代器  
Iterator仅用于遍历集合，Iterrator本身并不存放对象
Iterator结构如下所示  
![Iterator](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309111511575.PNG)  
  
hasNext用于检测是否还有下一个元素,next用于取出，每次调用next必须使用hasNext检测
```java
ctrl+j 快捷键提示
ctrl+alt+t 代码块
itit iterator的while循环快捷键
I 增强for快捷键
```
遍历结束后，迭代器指向最后的元素，若想重新遍历，需要重新调用iterator()方法

##### 增强for循环
本质上是简化的iterator,只可以用来遍历集合和数组  
前面记录过了具体使用，此处略  

### List
List接口是Collection接口的子接口  
1. List集合类中元素有序（添加顺序和取出顺序一致），且可重复
2. List集合中的每个元素都有其对应的顺序索引

#### ArrayList
ArrayList可以加入所有元素，甚至是null值且也可以是多个null  
ArrayList底层是由数组来实现数据存储的
ArrayList基本等同于Vector，除了ArrayList是线程不安全的（执行效率高）多线程情况下，不建议使用  
##### ArrayList扩容机制
1. ArrayList中维护了一个Object类型的数组elementData
2. 当创建ArrayList对象时，如果使用的是无参构造器，则初始elementData容量为0，第一次添加，扩容10，若需再次扩容，扩容1.5倍
3. 如果使用指定大小的构造器，则初始elementData容量为指定大小，若需再次扩容，扩容1.5倍

#### Vector（ArrayList安全版）
ArrayList底层也是由数组来实现数据存储的  
Vector是线程同步即线程安全的,效率较低，在开发中，若需要线程同步安全时，考虑使用Vector  
1. 无参构造，默认10，满后，2倍扩容
2. 有参构造，每次两倍扩容  
但其实应该可以指定扩容大小，源码中有设置

#### LinkedList
LinkedList底层实现了双向链表和双端队列特点  
可以添加任意元素（元素可以重复，包括null）  
线程**不安全**，**没有实现同步**  
##### LinkedList 操作机制
1. LinkedList 底层维护了一个双向链表
2. LinkedList 中有两个属性first和last分别指向首节点和尾节点
3. 每个节点（Node对象）里面又维护了prev、next、item三个属性，其中prev指向前一个节点，next指向后一个节点
4. 所以LinkedList的元素添加和删除，不是通过数组完成的，相对来说效率较高
### Set
1. 无序（添加和取出顺序不一致,生成后，取出顺序不会变），无索引，即无法用普通for循环
2. 不允许重复元素，最多一个null
#### HashSet
HashSet接口实现了Set接口  
HashSet底层实际上是HashMap（数组+链表+红黑树，链表到达一定量，数组大小在一定范围，树化）  
不允许重复元素，可以存放null值，但是只能有一个null  
HashSet不保证元素有序，取决于hash后，再确定索引结果
![HashSet](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309131312985.PNG)

##### LinkedHashSet
HashSet的子类  
底层是一个LinkedHashMap（HashMap子类），底层维护了一个数组+双向链表,添加以及扩容等机制和HashSet类似，只不过用的是双向链表   
LinkedHashSet根据元素的hashcode值来决定元素的存储位置，同时用链表维护元素的次序，这使得元素看起来是以**插入顺序保存**的  
LinkedHashSet不允许添加重复元素
![LinkedHashSet](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309131521987.PNG)  

#### TreeSet
TreeSet继承TreeMap  
当我们使用无参构造器时，创建的TreeSet依旧无序  
可以通过传入比较器，从而使之有序  
```java
TreeSet treeSet = new TreeSet(new Comparator() {
    @Override
    public int compare(Object o1, Object o2) {
        //调用String类的比较方法
        return ((String) o1).compareTo((String) o2);
    }
});
treeSet.add("jack");
treeSet.add("apple");
treeSet.add("dom");
treeSet.add("split");
treeSet.add("zoo");
System.out.println(treeSet);
```
注意当TreeSet传入比较器后，是否加入元素由其决定，比如比较器改为长度比较，则相同长度的元素只能加入一个  
当TreeSet未传入比较器时，会自动调用key所属于的类的Comparable接口，实现去重,如果该类没有实现Comparable接口，会抛出异常  
```java
TreeSet treeSet = new TreeSet();
treeSet.add(new Person());
class Person{}
//Exception in thread "main" java.lang.ClassCastException: Person cannot be cast to java.lang.Comparable
```
### Map
用于保存具有映射关系的数据（双列）：Key-Value  
Map中的Key和Value可以是任何引用类型的数据，会被封装到HashMap$Node对象中    
Map中key不可以重复，value可以重复，当key重复，value值将会被后来值覆盖即替换  
Map中key可以为空但仅有一个，value可以为空但有多个
```java
Map map = new HashMap();
map.put("no1","hhh");
map.get("no1"); 
```
为方便程序员遍历，创建entrySet集合，集合里存放Entry  
keySet:获取所有键key  
entrySet:获取所有关系k-v  
values:获取所有的值value  
![Map1](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309141411821.PNG)
示意图：
![Map2](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309141411660.PNG)

#### Map遍历方式
HashMap**没有实现同步，线程不安全**
```java
Map map = new HashMap();
map.put("no1",1);
map.put("no2",2);
map.put("no3",3);
map.put("no4",4);
map.put("no5",5);
map.put("no6",6);
//第一组：先取出所有的Key，通过Key取Value
Set set = map.keySet();
//1.增强for:
for (Object key:set) {
    System.out.println(key+":"+map.get(key));
}
//2.迭代器
System.out.println("=================");
Iterator iterator = set.iterator();
while (iterator.hasNext()) {
     Object key =  iterator.next();
     System.out.println(key+":"+map.get(key));
}
//第二组：直接通过Values取出所有Value
Collection values = map.values();
//1.增强for 略
//2.迭代器  略

//第三组: 通过EntrySet来获取
//1.增强for
System.out.println("=================");
Set set1 = map.entrySet();//EntrySet里面是Map.Entry
for (Object entry:set1) {
      //所以此处需要先转换
      Map.Entry m=(Map.Entry)entry;
      System.out.println(m.getKey()+":"+m.getValue());//用Entry的方法getKey和getValue
}
//2.迭代器
System.out.println("=================");
Iterator iterator1 = set1.i terator();
while (iterator1.hasNext()) {
    Object next =  iterator1.next();//本质是HashMap$Node，但若转换为Node，没有相应方法取键值
    Map.Entry entry=(Map.Entry)next;//所以转换为Entry
    System.out.println(entry.getKey()+":"+entry.getValue());
}
```
#### Hashtable HashMap安全版
1. 存放的元素是键值对
2. Hashtable的键和值都不能为NULL,否则会抛出异常
3. Hashtable使用方法基本上和HashMap一样
4. Hashtable是线程安全的，HashMap线程不安全  

扩容机制:  
* 底层有数组 Hashtable$Entry[] 初始化大小为 11
* 临界值 threshold 8=11*0.75
* 执行方法 addEntry来添加k-v 封装到Entry
* 当达到临界值时 新容量=(旧容量<<1)+1 也就是*2+1

##### Properties
1. Properties类继承自Hashtable类并且实现了Map接口，也是使用一种键值对的形式来保存数据,k-v不能为null
2. 它的使用特点和Hashtable类似
3. Properties还可以用于从 xxx.properties 文件中,加载数据到Properties类对象，并进行读取和修改
4. 工作后 xxx.properties 文件通常作为配置文件，这个知识点会在IO流中举例。

#### TreeMap
和TreeSet类似操作,只不过是键值对,略

### 如何选择集合
![选择集合](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309181428108.PNG)

### Collections工具类
Collections是一个操作 Set、List、Map等集合的工具类  
Collections提供了一系列静态的方法对集合元素进行排序、查询和修改等操作  




