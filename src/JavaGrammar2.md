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


//数组排序
Integer[] array = {1, 2, 3};
Arrays.sort(array,Comparator.reverseOrder());//降序排列
Arrays.sort(array,Comparator.naturalOrder());//自然升序
Arrays.sort(array);//默认升序
```
在 Java 中，`String` 对象的比较大小通常使用 `compareTo()` 方法。该方法比较两个字符串的字典顺序，并返回一个整数值，表示两个字符串的大小关系。

- 如果字符串相等，`compareTo()` 返回值为 0。
- 如果调用字符串按字典顺序排在参数字符串之前，则返回值为负数。
- 如果调用字符串按字典顺序排在参数字符串之后，则返回值为正数。

此外，Java 中的 `String` 类还提供了 `compareToIgnoreCase()` 方法，该方法在比较字符串时会忽略大小写。

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
        +"年"+(instance.get(Calendar.MONTH)+1)  //月份从0开始所以要+1
        +"月"+instance.get(Calendar.DAY_OF_MONTH)+"日" 
        +instance.get(Calendar.HOUR_OF_DAY)+":"
        +instance.get(Calendar.MINUTE)+":"
        +instance.get(Calendar.SECOND));
//上面默认是12小时制，若要改为24小时制，将Calendar.HOUR改为Calendar.HOUR_OF_DAY即可
instance.get(Calendar.HOUR);
instance.get(Calendar.HOUR_OF_DAY);
//可以看到括号里还可以写数字，因为Calendar类中数字对应了属性
```
### 第三代日期
前面两代日期都存在问题，比如Calendar是可变的,但日期和时间应为不可变  
又比如Date中年份从1900开始，Calendar月份从0开始，Calendar也没有格式化的工具  
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

JUC中也有一个ArrayList的安全实现，copyonwriteArrayList

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

// 该方法试图查找键对应的值，若存在返回对应值，若不存在返回默认值
map.getOrDefault(key,defaultValue)
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

```
Hashtable在Java中是一个非常早期的数据结构，它提供了线程安全的键值对映射。然而，Hashtable的性能不如一些其他的数据结构，如HashMap，主要有以下几个原因：

同步锁：Hashtable的所有公共方法都是同步的，这意味着在多线程环境中，任何时候只有一个线程可以访问Hashtable。这种全局锁的机制在高并发环境下会成为性能瓶颈，因为其他线程都必须等待当前操作完成才能进行下一次操作。

无法接受null：Hashtable不允许键或值为null，这在某些情况下可能会限制其使用。
```

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
传统方法读取配置：  
```java
public void readMysql() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader("src\\mysql.properties"));//根目录下查找
    String Line="";
    while((Line=bufferedReader.readLine())!=null){
        String sp[]=Line.split("=");//切割字符串
        System.out.println(sp[0]+"="+sp[1]);
    }
    bufferedReader.close();
}
```
如何更简便读取？  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091516235.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091517332.PNG)  
读取示例:  
```java
public void readMysql() throws IOException {
    Properties properties = new Properties();
    //加载指定配置文件到properties对象
    properties.load(new FileReader("src\\mysql.properties"));
    //将k-v 显示到显示器
    properties.list(System.out);
    //根据k获取v
    String user=properties.getProperty("user");
    String pwd=properties.getProperty("password");
    System.out.println(user);
    System.out.println(pwd);
}
public void setMysql() throws IOException {
    Properties properties = new Properties();
    //加载指定配置文件到properties对象
    properties.load(new FileReader("src\\mysql.properties"));
    //修改
    properties.setProperty("user","balance");
    properties.setProperty("charset","utf-8");
    //将修改保存
    properties.store(new FileOutputStream("src\\mysql.properties"),null);
}
```
#### TreeMap
和TreeSet类似操作,只不过是键值对,略

### 如何选择集合
![选择集合](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309181428108.PNG)

### Collections工具类
Collections是一个操作 Set、List、Map等集合的工具类  
Collections提供了一系列静态的方法对集合元素进行排序、查询和修改等操作  

## stream流 类似js函数式编程
Java Stream（流）是 Java 8 引入的一个重要特性，它用于对集合数据进行函数式操作和处理。Stream 提供了一种更便捷、更清晰、更高效的方式来操作集合数据，而不需要显式迭代或编写循环。  
### 创建
#### 集合创建流
```java
List<String> list = new ArrayList<>();
list.stream();

Set<String> set = new HashSet<>();
set.stream();

Map<String,String> map = new HashMap<>();
map.keySet().stream();
map.values().stream();
map.entrySet().stream();
```
#### 数组创建流
```java
String[] arr = {};
Arrays.stream(arr);
```
#### Stream的静态方法
Stream.of(T t) 该方法用于创建一个包含指定元素的 Stream，可以传入一个可变数量的参数  
Stream.of(T... values)  
Stream.iterate(T seed,UnaryOperator<T> f)：生成一个无限长度的Stream，参数一是初始值，参数二是函数，作用于参数一。一般与limit连用，限制元素个数。  
Stream.generate(Supplier<T> s)：生成一个无限长度的Stream，一般与limit连用。**常用场景：生成常量流和随机数流。**  
```java
Stream.of(1);
Stream.of(1,2,3);
Stream.iterate(0,(e->e+1)).limit(5).forEach(e-> System.out.println(e));
Stream.generate(()->Math.random()).limit(5).forEach(e-> System.out.println(e));
```
### 中间使用
每次返回新的流，可以有多个
#### 无状态 元素处理不受之前元素影响
##### filter
对流中的元素按照给定的函数过滤，生成新的符合过滤条件的流。
```java
Integer[] arr = {1,2,3,4,5,6,7,8,9,10};
Stream.of(arr).filter(e->e>=5).forEach(e-> System.out.println(e));
```
```json
//输出：
5
6
7
8
9
10
```
##### map
对流中的每个元素按照给定的函数进行转换操作，生成新的流只包含转换后的元素。
```java
String[] arr = {"apple","banana","orange"};
Stream.of(arr).map(e ->e.length()).forEach(e->System.out.println(e));
```
```json
//输出：
5
6
6
```
##### flatMap

1. **map() 方法**：
   - `map()` 方法用于将流中的每个元素按照指定的函数进行映射转换，将原始流中的每个元素通过指定的函数映射为另一个元素。
   - `map()` 方法返回的是一个新的流，**该流中的每个元素都是通过指定的映射函数转换得到的**。
   - `map()` 方法适用**于一对一的转换，即原始流中的每个元素都只映射为一个元素**。
2. **flatMap() 方法**：
   - `flatMap()` 方法用于将流中的每个元素映射为一个流，然后将这些流连接成一个流。
   - `flatMap()` 方法**返回的是一个新的流，该流中的元素是原始流中的每个元素映射得到的流中的所有元素连接而成的。**
   - `flatMap()` 方法适用于**一对多的转换，即原始流中的每个元素都可以映射为一个包含多个元素的流**。

下面是 `map()` 和 `flatMap()` 方法的对比示例：

```java
javaCopy codeList<String> words = Arrays.asList("Java", "is", "awesome");

// 使用 map() 方法将每个字符串转换为其长度
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());
System.out.println("Lengths: " + lengths);  // Output: Lengths: [4, 2, 7]

// 使用 flatMap() 方法将每个字符串拆分为字符流，然后连接成一个流
List<Character> characters = words.stream()
    .flatMap(word -> word.chars().mapToObj(c -> (char)c))
    .collect(Collectors.toList());
System.out.println("Characters: " + characters);  
// Output: Characters: [J, a, v, a, i, s, a, w, e, s, o, m, e]
```

在上面的示例中，`map()` 方法将每个字符串映射为其长度，得到一个包含字符串长度的流；而 `flatMap()` 方法将每个字符串映射为其字符流，然后将这些字符流连接成一个流，得到一个包含字符的流。

**flatMapToInt()、flatMapToLong()、flatMapToDouble()是flatMap()的是三个变种方法，主要作用是免除自动拆箱装箱的额外消耗。**  
```json
String[] arr = {"1","2","3","4","5","6"};
Stream.of(arr).flatMapToDouble(e-> DoubleStream.of(Double.parseDouble(e))).forEach(e-> System.out.println("value："+e+"，type："+ToolUtil.getType(e)));

//输出：
value：1.0，type：class java.lang.Double
value：2.0，type：class java.lang.Double
value：3.0，type：class java.lang.Double
value：4.0，type：class java.lang.Double
value：5.0，type：class java.lang.Double
value：6.0，type：class java.lang.Double
```
##### peek
将流中的元素进行处理，不改变流中的元素。  
peek()与map()的区别：map操作可以改变流中的元素，有返回值；peek操作仅仅在操作中消费元素，没有返回值，传入下一个操作的元素不会改变  
```java
String[] arr = {"1","2","3","4","5","6"};
Stream.of(arr).map(e-> {
    e = e + "a";
    System.out.println("map:"+e);
    return e;
}).forEach(e-> System.out.println("map-foreach:"+e));

Stream.of(arr).peek(e-> {
    e = e + "a";
    System.out.println("peek:"+e);
}).forEach(e-> System.out.println("peek-foreach:"+e));
```
```json
//输出：
map:1a
map-foreach:1a
map:2a
map-foreach:2a
map:3a
map-foreach:3a
map:4a
map-foreach:4a
map:5a
map-foreach:5a
map:6a
map-foreach:6a
peek:1a
peek-foreach:1
peek:2a
peek-foreach:2
peek:3a
peek-foreach:3
peek:4a
peek-foreach:4
peek:5a
peek-foreach:5
peek:6a
peek-foreach:6
```
##### unordered
基于调用流，返回一个无序流。  
在有序流的并行执行情况下，保持 的顺序性是需要高昂的缓冲开销。所以在处理元素时，不需要保证元素的顺序性，那么我们可以使用 unordered() 方法来实现无序流。
#### 有状态 必须拿到所有元素才能继续下去
##### distinct 去重
将流中的元素去重。如果是自定义类，一定要重写equals()方法与hashCode()方法。
##### sorted 排序
默认按自然升序对集合进行排序，可使用Comparator提供 reverseOrder() 方法实现降序排列。
```java
String[] arr = {"7","2","0","4","2","6"};
Stream.of(arr).sorted().forEach(e-> System.out.print(e+"->"));
System.out.println();
Stream.of(arr).sorted(Comparator.reverseOrder()).forEach(e-> System.out.print(e+"->"));
System.out.println();
Stream.of(arr).sorted(new Comparator<String>() {
    @Override
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
}).forEach(e-> System.out.print(e+"->"));
```
```json
//输出：
0->2->2->4->6->7->
7->6->4->2->2->0->
7->6->4->2->2->0->
```
##### limit
截取前n个元素，返回新的stream流。
##### skip
跳过前面n个元素，返回新的stream流。
### 终止
每个流只能进行一次终止操作，终止操作结束后流无法再使用。终止操作会产生新的集合或者值。  
#### 非短路操作（必须处理完所有操作才能得到结果)
##### forEach 不一定按序
遍历。在并行操作中，这种方法不能保证按顺序执行。
##### forEachOrdered 按序
遍历。保证了在顺序流和并行流中都按顺序执行。
##### toArray
将流转为数组。
##### reduce
是一个规约操作，所有的元素归约成一个结果值。三种调用方法，如下：  
1. `Optional<T> reduce(BinaryOperator<T> accumulator)`：一个参数，这个参数的名称accumulator（累加器），它的类型是一个函数式接口，这个接口是继承了BiFunction<T,U,R>，实现这个接口有两个输入，一个T类型，一个U类型，返回值是R类型  
2. `T reduce(T identity, BinaryOperator<T> accumulator)`：两个参数。第一个参数是初始值（限制必须是流元素类型），第二个参数跟方法一中参数一致。  
3. `<U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)`：三个参数。前两个参数是对方法二的改进，此方法可以自定义初始值和返回值的类型。第三个参数用来合并并行流，也就是说只有使用了并行流，第三个参数才有意义。  
```java
String[] arr = {"1","2","3","4","5","6"};
Optional<String> result1 = Stream.of(arr).reduce((e1, e2)->e1+e2);
String result2 = Stream.of(arr).reduce("",(e1,e2)->e1+e2);
Integer result3 = Stream.of(arr).reduce(0,(e1,e2)->Integer.valueOf(e1)+Integer.valueOf(e2),(arr1,arr2)->Integer.valueOf(arr1)+Integer.valueOf(arr2));

System.out.println(result1);
System.out.println(result2);
System.out.println(result3);
```
```json
//输出：
Optional[123456]
123456
21
```
##### collect

##### max
取最大值  
```java
String[] arr = {"apple","banana","waltermaleon"};
System.out.println(Stream.of(arr).max((e1,e2)-> {
            return e1.length() - e2.length();
        }).get());

//输出：
//waltermaleon
```
##### min
取最小值  
```java
String[] arr = {"apple","banana","waltermaleon"};
System.out.println(Stream.of(arr).min((e1,e2)-> {
            return e1.length() - e2.length();
        }).get());

//输出：
//apple
```
##### count 取个数
取流中元素的个数  
```java
String[] arr = {"7","2","0","4","2","6"};
System.out.println(Stream.of(arr).count());
//输出：
//6
```
#### 短路操作（遇到符合条件的元素就可以得到最终结果）
##### anyMatch
任意匹配。对流中的元素进行判断，有任意一个匹配，则返回ture。否则返回false。  
allMatch()：完全匹配。对流中所有的元素进行判断，如果都满足返回true，否则返回false。  
noneMatch()：不匹配。判断数据流中没有一个元素与条件匹配的，返回true，否则返回false。  
```java
String[] arr = {"banana","peach","apple","orange", "waltermaleon", "grape"};
System.out.println(Stream.of(arr).anyMatch(e->e.equals("apple"));
```
##### findFirst
获取流中的第一个元素  
findAny()：获取流中任意一个元素，搭配parallel使用，才会生效。  
```java
String[] arr = {"banana","peach","apple","orange", "waltermaleon", "grape"};
Stream.of(arr).filter(e->e.length() == 5).parallel().findFirst().ifPresent(e-> System.out.println(e));

//输出：
//peach
```
