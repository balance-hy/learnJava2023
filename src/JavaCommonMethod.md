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

## Math
|   方法   |    作用    |
|:------:|:--------:|
|  abs   |   绝对值    |
|  pow   |   幂运算    |
|  ceil  |   向上取整   |
| floor  |   向下取整   |  
| round  |   四舍五入   |  
|  sqrt  |   平方根    | 
| random |   求随机数   |  
|  max   | 求两个数中较大值 |  
|  min   | 求两个数中较小值 | 

## Arrays
|      方法      |                         作用                          |
|:------------:|:---------------------------------------------------:|
|   toString   |                      数组转字符串输出                       |
|     sort     |         排序（可定制排序规则，传入new Comparator()匿名内部类）         |
| binarySearch |                        二分查找                         |
|    copyOf    | 数组元素复制(arrName,length)，length超过会填入null，底层是arraycopy |  
|     fill     |        数组元素填充(arrName,element)，用elelment填充数组        |  
|    equals    |                    比较两数组元素是否完全相同                    | 
|    asList    |                     将一组值转为list                      |
```java
Integer i[]={99,11,-1,100,6};
Arrays.sort(i, new Comparator() {
    @Override
    public int compare(Object o1, Object o2) {
        Integer o11 = (Integer)o1;
        Integer o21 = (Integer)o2;
        return o11-o21;//从小到大，类似C的qsort重写
    }
});
System.out.println(Arrays.toString(i));

//asList
Integer i[]={99,11,-1,100,6};
List<Integer> asList=Arrays.asList(i);
System.out.println(asList);
```
## System
|         方法         |         作用         |
|:------------------:|:------------------:|
|        exit        |       退出当前程序       |
|     arraycopy      |   复制数组元素，适合底层调用    |
| currentTimeMillens | 返回当前时间距19701-1的毫秒数 |
|         gc         |      运行垃圾回收机制      |

## BigInteger

|    方法    |     作用     |
|:--------:|:----------:|
|   add    |     相加     |
| subtract |     相减     |
| multiply |     相乘     |
|  divide  |     相除     |


## BigDecimal
|    方法    |   作用    |
|:--------:|:-------:|
|   add    |   相加    |
| subtract |   相减    |
| multiply |   相乘    |
|  divide  |   相除    |

注意因为是高精度，所以相除若除不尽，会出现异常  
此时指定精度即可，例如  
```java
divide(bigDecimal1,bigDecimal2.ROUND_CEILING);//保留到分子的精度
```
## Collection
|     方法      |     作用     |
|:-----------:|:----------:|
|     add     |   添加单个元素   |
|   remove    |   删除指定元素   |
|  contains   |  查找元素是否存在  |
|    size     |   获取元素个数   |
|   isEmpty   |   判断是否为空   |
|    clear    |     清空     |
|   addAll    |   添加多个元素   |
| containsAll | 查找多个元素是否存在 |
|  removeAll  |   删除多个元素   |
### List
|     方法      |          作用          |
|:-----------:|:--------------------:|
|     add     |        指定位置添加        |
|   addAll    |      指定位置添加多个元素      |
|     get     |       获取指定位置元素       |
|   indexOf   |   返回obj在集合中首次出现位置    |
| lastIndexOf |   返回obj在集合中末次出现位置    |
|   remove    | 移除指定index位置元素，并返回此元素 |
|     set     |    设置指定位置元素，相当于替换    |
|   subList   |   返回从指定位置到指定位置的子集合   |

#### LinkedList
|   方法   |     作用      |
|:------:|:-----------:|
|  add   |    添加节点     |
| remove | 删除节点，默认删首节点 |
|  set   |   修改某个节点    |
|  get   |   得到某个节点    |

### Set
|   方法    |   作用   |
|:-------:|:------:|
|   add   |   添加   |
| remove  |   删除   |
| isEmpty | 判断是否为空 |
具体自己看类方法

## Map
|     方法      |       作用       |
|:-----------:|:--------------:|
|     put     |       添加       |
|   remove    |  根据key删除映射关系   |
|     get     | 根据键获取值,返回用对象接收 |
|    size     |     获取元素个数     |
|    clear    |       清空       |
|   isEmpty   |    判断个数是否为0    |
| containsKey |    查找键是否存在     |