## 泛型 ~广泛引用类型
**传统方法有哪些问题**：  
1. 不能对加入到集合(例如ArrayList)中的数据类型进行约束(不安全),即不能规定加入的类型  
2. 遍历的时候，需要进行类型转换，如果集合中的数据量较大，对效率有影响，默认是Object类型  

**使用泛型的好处**：
1. 编译时，检查添加元素的类型，提高了安全性
2. 减少了类型转换的次数，提高了效率
```java
//规定为List中只能为Dog类型,或者为Dog子类
ArrayList<Dog> dogs = new ArrayList<Dog>();
```
### 泛型作用：
可以在类声明时通过一个标识表示类中某个属性的类型，或者是某个方法的返回值的类型，或者是参数类型  
```java
class Person<E>{
    E s;//用E来代替类型，该数据类型在实例化Person对象时候指定，即编译时
    public Person(E s) {//可以是参数类型
        this.s = s;
    }
    public E f(){//可以是方法的返回值的类型
        return s;
    }
}
//具体使用
Person<String> stringPerson = new Person<String>("name");
```
### 泛型声明：  
```java
interface 接口<T>{} 和 class 类<K,V>{}
```
**其中，T,K,V不代表值，而是表示类型，任意字母其实都是可以的，只不过常用这些,给泛型指定类型时只能是引用类型不能是基本数据类型**  
泛型使用：  
```java
ArrayList<String> strings = new ArrayList<>();//一般简写，即只写左边

//泛型默认是Object 下面等价于ArrayList<Object> objects = new ArrayList<>();
ArrayList arrayList = new ArrayList(); 
```
### 自定义泛型
#### 自定义泛型类
```java
class 类名<T,R...>{//...表示可以有多个泛型
    
}
```
注意事项:  
1. 普通成员可以使用泛型（属性、方法）
2. 类中使用泛型的数组，不能初始化 **因为数组在new时，不能确定类型，不知道开多大的空间适合**
3. 静态属性、方法中不能使用类的泛型 **静态和类相关，在类加载时，对象还没有创建**
4. 泛型类的类型，是在创建对象时确定的
5. 如果在创建对象未指定类型，默认为Object  

#### 自定义泛型接口
```java
class 类名<T,R...>{
    
}
```
注意事项:  
1. 接口中，静态成员也不能使用泛型，原因和类一样
2. 泛型接口的类型，在继承接口或者实现接口时确定
3. 没有指定类型，默认为Object 

#### 自定义泛型方法
```java
修饰符 <T,R...> 返回类型 方法名(参数){
    
}
//泛型标识符可以直接提供给方法名中参数使用
public <T,R> void fly(T t,R r){
    
}
//编译器自动确认类型
A a = new A();
a.<Dog,Integer>fly(new Dog(),1);//无需写<Dog,Integer>
class A{
    public <T,R> void fly(T t,R r){
        System.out.println(t);
        System.out.println(r);
    }
}
class Dog{

}
```
注意事项：  
1. 泛型方法可以定义在普通类中，也可以定义在泛型类中  
2. 当泛型方法被调用时，类型会确定  
```java
public void eat<E,e>{
    
}
```
**上面代码中修饰符后没有<T,R...>,所以eat方法不是泛型方法，而是使用了泛型**  
### 泛型继承和通配符
泛型不具备继承性
```java
List<Object> list=new ArrayList<String>();//String是Object子类,但会报错
```
1. <?>:支持任意泛型类型
2. <? extends A>:支持A类以及A类的子类
3. <? super A>:支持A类和A类的父类，不限于直接父类
### Junit
在方法上加`@Test`注解，引入依赖后，左侧边栏会出现三角执行按钮，就可以单独执行来测试了。

## 线程基础
### 进程
![process](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309211520556.PNG)

### 线程
![thread](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309211526939.PNG)
#### 创建线程
##### 继承Thread类
方式一：继承Thread(实现了Runnable接口)类，重写run方法
![thread](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309241513084.PNG)
当一个类继承了Thread类，该类就可以当线程使用  
我们会重写run方法，在其中写上自己的业务代码  
```java
public class Demo1 {
    public static void main(String[] args) throws InterruptedException {
        Cat cat = new Cat();
        cat.start();//底层由jvm调用了start0方法，创建新线程，注意不能直接调run方法，这样还是在主线程之中
        for(int i=0;i<10;i++){
            Thread.sleep(1000);
            System.out.println(i+" "+Thread.currentThread().getName());//线程main
        }
    }
}
class Cat extends Thread{
    int times=0;
    @Override
    public void run() {
        while(true) {
            System.out.println("this is "+Thread.currentThread().getName());//线程Thread0
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            times++;
            if (times == 10) {
                break;
            }
        }
    }
}
```
##### 实现Runnable接口
方式二：实现Runnable接口，重写run方法  
为什么需要这种方法？因为：
1. java是单继承的，在某些情况下一个类可能已经继承了某个父类，这时就无法再继承Thread类了
2. 此时可以通过实现Runnable接口来创建线程  

```java
public class Demo1 {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        //dog.start();//报错，无start方法
        //dog.run();//正确，但这时main线程，并未多创建线程
        Thread thread = new Thread(dog);//创建线程，为什么可以把dog放进去？
        thread.start();//因为这里底层使用了设计模式【代理模式】
    }
}
class Dog implements Runnable{
    int times=0;
    @Override
    public void run() {
        while(true){
            System.out.println("this is a dog and is "+Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            times++;
            if(times==10){
                break;
            }
        }
    }
}
```
![区别](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309241647375.PNG)
这里不是说就不可以通过继承Thread来操作同一资源了，比如：
![threadMore](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309241654164.PNG)
这就是一个类多次实例化创建不同线程操作同一资源的例子  
#### 线程终止
1. 当线程完成任务后，会自动退出
2. 还可以通过使用变量来控制run方法终止的方式结束线程

简单来说之前是while循环，在主线程设置flag为false，让while循环结束就可以控制子线程退出  
##### yield
线程的礼让，让出cpu，让其他线程执行，但礼让的时间不确定，所以不一定礼让成功。但其实取决于cpu，如果资源够多
就不会出现让步的效果，因为此时同时执行也是可以的。

##### join
线程插队。插队的线程一旦插队成功，则肯定先执行完插入的线程所有的任务。

#### 用户线程、守护线程
**用户线程也叫工作线程**，线程的任务执行完或通知方式来结束  
**守护线程一般是为工作线程服务的**，当所有的用户线程结束，守护线程自动结束  
常见的守护线程：垃圾回收机制  
```java
线程名.setDaemon(true);
public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();

        Thread thread = new Thread(dog);
        thread.setDaemon(true);
        thread.start();
}
//此时主线程结束，thread线程结束        
```
#### 线程状态
![threadState](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202309261426513.PNG)
线程状态。 线程可以处于以下状态之一：
* NEW
* 尚未启动的线程处于此状态。
* RUNNABLE
* 在Java虚拟机中执行的线程处于此状态。
* BLOCKED
* 被阻塞等待监视器锁定的线程处于此状态。
* WAITING
* 正在等待另一个线程执行特定动作的线程处于此状态。
* TIMED_WAITING
* 正在等待另一个线程执行动作达到指定等待时间的线程处于此状态。
* TERMINATED
* 已退出的线程处于此状态。
* 一个线程可以在给定时间点处于一个状态。 这些状态是不反映任何操作系统线程状态的虚拟机状态。  

#### 线程同步
在多线程编程时，一些敏感数据不允许被多个线程同时访问，此时就使用同步访问技术
保证数据在同一时刻，最多有一个线程访问，以保证数据完整性。  
具体方法-**Synchronized**  
1.同步代码块，推荐，范围小，效率相对高
```java
synchronized (){//得到对象的锁，才能操作代码
    //需要被同步代码
}
```
2.方法声明中，表示整个方法为同步方法  
```java
public synchronized void m(String name){
    //需要被同步代码
}
```
### 锁
#### 互斥锁
1. java语言中，引入了对象互斥锁的概念，来保证共享数据操作的完整性
2. 每个对象都对应于一个可称为“互斥锁”的标记，这个标记用来保证在任何时刻只有一个线程访问该对象
3. 关键字synchronized来与对象的互斥锁联系。当某个对象用synchronized修饰时，表明该对象在任一时刻只能由一个线程访问
4. 同步局限性：会导致程序的执行效率变低
5. 同步方法（非静态）的锁可以是this，可以是其他对象。（**都需保证为同一对象**）
6. 同步方法（静态）的锁为当前类本身。xxx.class  
```java
synchronized (this/其他对象){
    //需要被同步代码
}
```

## IO流
### 文件
#### 创建文件
|                  方法                  |       作用       |
|:------------------------------------:|:--------------:|
|      new File(String pathname)       | 根据路径构建一个File对象 |
|  new File(File parent,String child)  |  根据父目录文件+子路径   |
| new File(String parent,String child) | 根据父目录+子路径构建文件  |
|         file.createNewFile()         |      创建文件      |
#### 获取文件信息
|           方法           |     作用     |
|:----------------------:|:----------:|
|     file.getName()     |   获取文件名    |
| file.getAbsolutePath() |   获取绝对路径   |
|    file.getParent()    |  获取文件父级目录  |
|     file.length()      | 获取文件大小（字节） |
|     file.exists()      |   文件是否存在   |
|     file.isFile()      |  是不是一个文件   |
|   file.isDirectory()   |   是不是目录    |
#### 创建目录
```java
//mkdir:创建一级目录 file.mkdir()
//mkdirs:创建多级目录 file.mkdirs()
//delete:删除空目录或文件
File file = new File("D:\\this.txt");
if(file.exists()){
    if(file.delete()){
        System.out.println("文件删除成功");
    }else{
        System.out.println("文件删除失败");
    }
}else{
    System.out.println("文件不存在");
}

File file1 = new File("D:\\this");
if(file1.exists()){
    if(file1.delete()){
        System.out.println("目录删除成功");
    }else{
        System.out.println("目录删除失败");
    }
}else{
    System.out.println("目录不存在");
}
```
### 节点流
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071353852.PNG)
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071359537.PNG)  

|        抽象基类        |     字节流      |  字符流   |
|:------------------:|:------------:|:------:|
|      **输入流**       | InputStream  | Reader |
|      **输出流**       | OutputStream | Writer |
Java IO流涉及40多个类，但都是从如上四个基类派生的  
由这四个类派生的子类命名都是以其父类名作为子类的后缀  
#### 字节流 二进制文件专用
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/1.png)
##### FileInputStream 
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231001211851.png)
```java
//read 使用示范
public void read1(){
    File file = new File("E:\\test.txt");
    FileInputStream fileInputStream=null;
    int readData=0;
    try {
        fileInputStream = new FileInputStream(file);
        //readData接受读取的字节
        while((readData=fileInputStream.read())!=-1){
            System.out.print((char)readData);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            //用完就关闭，以免造成资源浪费
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
//read 数组接收
public void read2(){
    File file = new File("E:\\test.txt");
    FileInputStream fileInputStream=null;
    byte[] bytes = new byte[8];
    int readLen=0;
    try {
        fileInputStream = new FileInputStream(file);
        //读到返回-1即文件末尾结束
        while((readLen=fileInputStream.read(bytes))!=-1){//注意bytes数组实际上每次从头写入
            System.out.print(new String(bytes,0,readLen));//使用String构造器，offset代表开始截取的位置
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }finally {
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
##### FileOutPutStream
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231001214859.png)
```java
public void write1(){
    //注意FileOutputStream如果文件不存在会创建。
    String filePath="E:\\test.txt";
    FileOutputStream fileOutputStream=null;
    try {
        //默认是覆盖，构造时添加true，代表文件末尾追加，但这只表示是一次读取且关闭，连续写是不会覆盖的
        fileOutputStream=new FileOutputStream(filePath,true);
        //方法1 写入一个字节
        fileOutputStream.write('a');
        //方法2 写入一个字符串
        String str="this is String";
        //因为write要求字符数组，用字符串的方法转化
        fileOutputStream.write(str.getBytes());
        //也可指定字符数组写入范围，如下写入前三个字节
        fileOutputStream.write(str.getBytes(),0,3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
```
#### 字符流
##### FileReader
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231005152716.png)
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231005153126.png)
```java
//与FileInputStream类似，仅写一种作为示例
public void reader1(){
     String filePath="E:\\test.txt";
     FileReader fileReader=null;
     int readLen=0;
     char data[]=new char[8];
     try {
         fileReader=new FileReader(filePath);
         while ((readLen=fileReader.read(data))!=-1) {
             System.out.print(new String(data,0,readLen));
         }
     } catch (IOException e) {
        throw new RuntimeException(e);
     }finally {
         try {
             fileReader.close();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
}
```
##### FileWriter
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231005153330.png)
![](https://raw.githubusercontent.com/balance-hy/typora/master/img/20231005153513.png)
```java
//与FileOutputStream类似
public void write1(){
   //注意FileWriter如果文件不存在会创建。
   String filePath="E:\\test1.txt";
   FileWriter fileWriter=null;
   try {
        fileWriter=new FileWriter(filePath);
        fileWriter.write('a');
        fileWriter.write("hhhh");
        fileWriter.write("我是谁",0,2);
        String str="怎么回事";
        fileWriter.write(str.toCharArray());
        fileWriter.write(str.toCharArray(),0,2);
   } catch (IOException e) {
        throw new RuntimeException(e);
   }finally {
       try {
            fileWriter.close();//一定要关闭或者刷新，否则还是在内存中，并未真正写入
       } catch (IOException e) {
            throw new RuntimeException(e);
       }
   }
}
```
### 处理流
处理流也称**包装流**，是“连接” 在已存在的流（节点流或处理流）之上，为程序提供更为强大的读写功能
如BufferedReader、BufferedWriter。其实，就是定义了一个接口对象，也就是多态的应用
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071356449.PNG)
#### 字节流 二进制文件专用
##### BufferedInputStream BufferedOutputStream
```java
@Test
public void readAndWrite() throws Exception {
    String srcPath="D:\\1.PNG";
    String destPath="D:\\2.PNG";
    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcPath));
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destPath));
    byte a[]=new byte[1024];
    int readLen=0;
    while((readLen=bufferedInputStream.read(a))!=-1){
        bufferedOutputStream.write(a,0,readLen);
    }
    bufferedInputStream.close();
    bufferedOutputStream.close();
}
```
##### ObjectInputStream 反序列化
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071539103.PNG)
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071541061.PNG)
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091329063.PNG)
```java
//注意，如果要使用dog对象，需要保证Dog类是在可访问的位置上，如果在不同包下无法访问会出现问题
public void objectOut() throws Exception {
    //指定反序列化文件
    String filePath="D:\\data1.dat";
    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));

    //读取（反序列化）的顺序需要和你保存数据（序列化）的顺序一致
    //否则会出现异常
    System.out.println(objectInputStream.readInt());
    System.out.println(objectInputStream.readBoolean());
    System.out.println(objectInputStream.readChar());
    System.out.println(objectInputStream.readDouble());
    System.out.println(objectInputStream.readUTF());
    Object dog=objectInputStream.readObject();
    System.out.println(dog);

    objectInputStream.close();
}
```

##### ObjectOutputStream 序列化
```java
public class Demo1 {
    public static void main(String[] args) throws InterruptedException {

    }
    @Test
    public void objectOut() throws Exception {
        //注意此时为dat后缀，序列化后的文件格式按照他的指定来
        String filePath="D:\\data.dat";
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        //注意这里如果直接write(100)是没有类型的
        objectOutputStream.writeInt(100);//int-> Integer(实现了Serializable接口) 自动装箱 下面同理
        objectOutputStream.writeBoolean(false);
        objectOutputStream.writeChar('a');
        objectOutputStream.writeDouble(3.12);
        objectOutputStream.writeUTF("我是谁");//注意字符串为writeUTF

        objectOutputStream.writeObject(new Dog());//注意对象所在类需要实现Serializable接口

        objectOutputStream.close();
    }
}
class Dog implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

#### 字符流
##### BufferedReader
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310071412965.PNG)  
关闭处理流时，只需要关闭外层流（所包装的节点流，系统会自动关闭）即可  
```java
@Test
public void read1() throws Exception{//所以在这里抛出
    String filePath="D:\\test.txt";
    BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));//这里会要求处理异常
    // 读取
    String line=null;
    //按行读取文件，高效，当返回null时文件读取完毕
    while((line=bufferedReader.readLine())!=null){
        System.out.println(line);
    }
    //只需关闭外层流
    bufferedReader.close();
}
```
##### BufferedWriter
```java
@Test
public void write1() throws IOException {//所以在这里抛出
    String filePath="D:\\test.txt";
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));//这里需要处理异常
    bufferedWriter.write("什么事情！");
    bufferedWriter.newLine();//插入一个与系统相关的换行
    bufferedWriter.write("什么事情！");
    bufferedWriter.newLine();
    bufferedWriter.write("什么事情！！！");
    bufferedWriter.newLine();

    bufferedWriter.close();
}
```
### 标准输入输出流
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091338261.PNG)
### 打印流
只有输出流，没有输入流  
#### printStream
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091436683.PNG)
```java
public void print1() throws IOException {
    PrintStream out=System.out;
    //默认输出即标准输出 显示器显示
    out.print("hello");
    //print 底层是write方法，所以也可以如下使用
    out.write("jello".getBytes());
    out.close();

    //可以修改默认输出，从而让其输出到文件
    System.setOut(new PrintStream("D:\\test2.txt"));
    System.out.println("heloo");
    System.out.close();
}
```
#### printWriter
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091437653.PNG)  
```java
public void print2() throws IOException {
    //PrintWriter printWriter=new PrintWriter(System.out);//默认输出到显示器
    PrintWriter printWriter=new PrintWriter(new FileWriter("D:\\test2.txt"));
    printWriter.print("fafaf");
    printWriter.close();//注意要关闭，否则不会写入文件
}
```
### 转换流
字节流->字符流  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091349304.PNG)  
当把文件编码从utf-8改为其他编码后，例如ANSI,再进行读取，会出现乱码问题，因为字符流默认按utf-8读取  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091351612.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091356956.PNG)  
#### InputStreamReader
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091355600.PNG)  
为了正确读取文件内容，可以用InputStreamReader包装（转换）字节流，指定读取编码格式  
```java
public void transformation() throws IOException {
    String filePath="D:\\test.txt";
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath),"gbk");
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String s=bufferedReader.readLine();
    System.out.println("内容="+s);
    bufferedReader.close();
}
```
#### OutputStreamWriter
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310091358067.PNG)  
为了写入指定的编码格式文件，可以用OutputStreamWriter包装（转换）字节流  
```java
public void transformation() throws IOException {
    String filePath="D:\\test1.txt";
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filePath),"gbk");
    outputStreamWriter.write("Hello，我是");//也可以再包装一层bufferedwriter
    outputStreamWriter.close();
}
```





