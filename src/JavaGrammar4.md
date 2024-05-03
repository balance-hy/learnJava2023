## 网络
### InetAddress类 ip相关
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310121411760.PNG)  
```java
public void InetAddress_() throws UnknownHostException {
    //获取本机 主机名和ip
    InetAddress inetAddress=InetAddress.getLocalHost();
    System.out.println(inetAddress);

    //根据获得的inetAddress对象，分别得到主机名和ip
    System.out.println(inetAddress.getHostName()+" "+inetAddress.getHostAddress());

    //根据指定主机名/域名 获得inetAddress对象
    InetAddress inetAddress1=InetAddress.getByName("www.baidu.com");//对于网站来说主机名=域名
    System.out.println(inetAddress1);//传递一个主机名，它将尝试解析主机名并返回与之关联的 IP 地址。
                                    // 如果你向该方法传递一个 IP 地址，
                                    // 它将返回一个 InetAddress 对象，其中包含了 IP 地址，但主机名字段将为 null。
    //根据指定IP地址 获得inetAddress对象
    String ip="153.3.238.110";
    String ip1[]=ip.split("\\.");
    byte b[]=new byte[4];
    for (int i = 0; i < ip1.length; i++) {
        b[i]=(byte)(Integer.parseInt(ip1[i]));
        System.out.println(b[i]);
    }
    InetAddress inetAddress2=InetAddress.getByAddress(b);
    System.out.println(inetAddress2);
}
```
### socket Tcp
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310121452160.PNG)  
#### TCP字节流
题目一：
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310141446923.PNG)  
```java
public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        OutputStream outputStream=socket.getOutputStream();//实际运行类型是SocketOutputStream
        System.out.println("outputStream ="+outputStream.getClass());
        //通过输出流，写入数据到数据通道
        outputStream.write("hello world".getBytes());

        //关闭流对象和socket
        outputStream.close();
        socket.close();
    }
}

public class SocketServer {
    public static void main(String[] args) throws IOException {
        //在本机的 9999端口监听，等待连接
        //细节：要求本机无其他服务在监听9999
        ServerSocket serverSocket = new ServerSocket(9999);
        //如果没有客户端连接时，程序会阻塞在这里
        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        byte a[]=new byte[1024];
        int readLen=0;
        while ((readLen=inputStream.read(a))!=-1){
            System.out.println(new String(a,0,readLen));
        }

        //关闭
        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
```
题目2：  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310141505964.PNG)  
类似，但注意，要设置output结束标志，不然会出错  
```java
public class SocketServer {
    public static void main(String[] args) throws IOException {
        //在本机的 9999端口监听，等待连接
        //细节：要求本机无其他服务在监听9999
        ServerSocket serverSocket = new ServerSocket(9999);
        //如果没有客户端连接时，程序会阻塞在这里
        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        byte a[]=new byte[1024];
        int readLen=0;
        while ((readLen=inputStream.read(a))!=-1){
            System.out.println(new String(a,0,readLen));
        }
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello client".getBytes());
        socket.shutdownOutput();//设置结束标志
        //关闭
        outputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}

public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        OutputStream outputStream=socket.getOutputStream();//实际运行类型是SocketOutputStream
        System.out.println("outputStream ="+outputStream.getClass());
        //通过输出流，写入数据到数据通道
        outputStream.write("hello world".getBytes());
        socket.shutdownOutput();//设置结束标志
        InputStream inputStream = socket.getInputStream();
        byte a[]=new byte[1024];
        int readLen=0;
        while((readLen=inputStream.read(a))!=-1){
            System.out.println(new String(a,0,readLen));
        }
        //关闭流对象和socket
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
```
#### TCP字符流
用转换流包装一下即可  
也可以用`writer.newLine()`作为结束标记，但注意此时需要保证对方使用`readLine()`读取字符  
```java
public class SocketServer {
    public static void main(String[] args) throws IOException {
        //在本机的 9999端口监听，等待连接
        //细节：要求本机无其他服务在监听9999
        ServerSocket serverSocket = new ServerSocket(9999);
        //如果没有客户端连接时，程序会阻塞在这里
        Socket socket = serverSocket.accept();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = bufferedReader.readLine();
        System.out.println(s);


        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("hello client");
        bufferedWriter.newLine();//设置结束标记，因为readLine读到此结束
        bufferedWriter.flush();//记得刷新，否则写入失败
        //关闭
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();
        serverSocket.close();
    }
}

public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("hello server");
        bufferedWriter.newLine();//设置结束标记，readLine读到换行符结束
        bufferedWriter.flush();//刷新，否则无法写入
        // BufferedWriter 会在缓冲区填满或者手动调用 flush() 方法时，将缓冲区的内容刷新到底层的输出流中。
        // 如果不调用 flush()，缓冲区的内容可能不会即时写入输出流，导致数据传输不完整，从而影响程序的运行

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = bufferedReader.readLine();
        System.out.println(s);

        //关闭流对象和socket
        bufferedWriter.close();
        bufferedReader.close();
        socket.close();
    }
}
```
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310141638424.PNG)  
注意这里面的小问题  
```java
public class SocketServer {
    public static void main(String[] args) throws IOException {
        //在本机的 9999端口监听，等待连接
        //细节：要求本机无其他服务在监听9999
        ServerSocket serverSocket = new ServerSocket(9999);
        //如果没有客户端连接时，程序会阻塞在这里
        Socket socket = serverSocket.accept();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("src\\2.PNG"));
        byte a[]=new byte[1024];
        int readLen=0;
        while((readLen=bufferedInputStream.read(a))!=-1){
            bufferedOutputStream.write(a,0,readLen);
        }


        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("收到图片");
        bufferedWriter.newLine();//设置结束标记，因为readLine读到此结束
        //按照源码bufferedWriter.close()时会自动刷新缓冲区，为什么还要显式flush()呢？
        //因为如果不显式flush()，当bufferedInputStream关闭时，就会关闭内层流，这会导致socket关闭
        //，bufferedWriter.close()再调用flush，就会导致错误的发生
        bufferedWriter.flush();//记得刷新，否则写入失败

        //关闭
        bufferedInputStream.close();
        bufferedOutputStream.close();
        bufferedWriter.close();
        socket.close();
        serverSocket.close();
    }
}

public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("D:\\1.PNG"));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        byte a[]=new byte[1024];
        int readLen=0;
        while((readLen=bufferedInputStream.read(a))!=-1){
            bufferedOutputStream.write(a,0,readLen);
        }
        //注意在这里需要先flush，否则会报Cannot send after socket shutdown错误
        //因为bufferedOutputStream.close()的逻辑是，如果此时缓冲区还有数据
        //还会调用一次flush，如果之前不flush，socket.shutdownOutput();之后就无法写入
        //从而抛出Cannot send after socket shutdown错误
        bufferedOutputStream.flush();
        socket.shutdownOutput();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = bufferedReader.readLine();
        System.out.println(s);

        //关闭流对象和socket
        bufferedInputStream.close();
        bufferedOutputStream.close();
        bufferedReader.close();
        socket.close();

    }
}
```
### UDP
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310151327701.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310151329017.PNG)
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310151327055.PNG)  
#### UDP实战
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310151332670.PNG)  
```java
public class udpReceive {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(9999);
        //UDP包最大为64k=64*1024，按需构造大小
        byte a[]=new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(a, a.length);
        System.out.println("准备接受数据");
        datagramSocket.receive(datagramPacket);

        //输出接收到的数据  datagramPacket.getData()实际接收到的数据 datagramPacket.getLength()实际接收到的数据长度
        System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getLength()));

        //输出数据报
        byte b[]="ok".getBytes();
        DatagramPacket datagramPacket1 = new DatagramPacket(b, b.length, InetAddress.getByName("10.20.108.9"), 9998);
        datagramSocket.send(datagramPacket1);

        //关闭资源
        datagramSocket.close();
    }
}

public class udpSender {
    public static void main(String[] args) throws IOException {
        //指定接收的端口
        DatagramSocket datagramSocket = new DatagramSocket(9998);
        byte a[]="hello udp receive".getBytes();//构建发送数组
        //构建数据报
        DatagramPacket datagramPacket = new DatagramPacket(a,a.length, InetAddress.getByName("10.20.108.9"),9999);
        datagramSocket.send(datagramPacket);//发送数据报

        //接收数据报 UDP包最大为64k=64*1024，按需构造大小
        byte b[]=new byte[1024];
        DatagramPacket datagramPacket1 = new DatagramPacket(b, b.length);
        System.out.println("准备接受数据");
        datagramSocket.receive(datagramPacket1);
        System.out.println(new String(datagramPacket1.getData(),0,datagramPacket1.getLength()));
        //关闭资源
        datagramSocket.close();
    }
}
```
## 反射
### 反射引出 
如何根据配置文件指定信息，创建对象并调用方法  
比如有一个文件re.properties:  
```java
classfullpath=com.balance.Cat
method=hi
```
解决:  
```java
Cat cat=new Cat();//传统方式
cat.hi();

//使用Properties读，可不可以
Properties properties = new Properties();
properties.load(new FileInputStream("src\\mysql.properties"));
String classfullpath = properties.get("classfullpath").toString();
String methodName = properties.get("method").toString();
//结果是不可以，因为无法new classfullpath()，classfullpath是字符串而非类名

//使用反射机制解决
//加载类，返回Class类型的对象
Class<?> aClass = Class.forName(classfullpath);
//通过 aClass.newInstance 获得你加载的类的对象实例
Object o = aClass.newInstance();
//通过 aClass.getMethod 获得你加载的类的 methodName的方法对象
// 即：在反射中，可以把方法视作对象
Method method = aClass.getMethod(methodName);
//通过method调用方法 ：即通过方法对象来实现调用方法
method.invoke(o);//传统 对象名.方法(),反射 方法.invoke(对象)
```
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310231438493.PNG)  
### 反射相关类
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310231500878.PNG)  
```java
//注意 getField 无法访问到私有属性
Field nameField = aClass.getField("name");
System.out.println(nameField.get(o));//传统写法 对象.属性 反射 属性.get(对象)

//默认得到无参构造
Constructor<?> constructor = aClass.getConstructor();
//通过指定参数的反射class，获得有参构造
Constructor<?> constructor1 = aClass.getConstructor(String.class);
```
### 反射的优缺点
优点：  
可以动态的创建和使用对象（也是框架底层核心），使用灵活，没有反射机制，框架技术就失去底层支撑。  
缺点：  
使用反射基本是解释执行，对执行速度有影响。  

### class类
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241333470.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241333958.PNG)  

#### 常用方法
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241346891.PNG)  

#### 获取class类对象的六种方式
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241412999.PNG)  
```java
Properties properties = new Properties();
properties.load(new FileInputStream("src\\mysql.properties"));
String classfullpath = properties.get("classfullpath").toString();
String methodName = properties.get("method").toString();

//1.Class.forName
Class<?> aClass = Class.forName(classfullpath);

//2.类名.class，应用场景：多用于参数传递
Class<udpReceive> udpReceiveClass = udpReceive.class;

//3.对象.getClass 应用场景：有对象实例
udpReceive udpReceive = new udpReceive();
Class<? extends com.balance.udp.udpReceive> aClass1 = udpReceive.getClass();

//4.通过类加载器来获取类的class对象
//（1）先得到类加载器
ClassLoader classLoader = udpReceive.getClass().getClassLoader();
//（2）通过类加载器得到class对象
Class<?> aClass2 = classLoader.loadClass(classfullpath);

//5.基本数据类型 直接 类型.class
Class<Integer> integerClass = int.class;

//6.基本类型对应的包装类 包装类.TYPE 实际上type和integerClass hashcode相同因为自动拆装箱
Class<Integer> type = Integer.TYPE;
```
#### 类加载
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241432675.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241436742.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241439268.PNG)  
##### 类加载各阶段
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241459231.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241500062.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241504479.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241507190.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241509373.PNG)  
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310241524345.PNG)  
#### 通过反射获取类结构信息
##### class
注意getConstructors无父类构造器，图片有误
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251419200.PNG)  

##### Field
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251437070.PNG)
##### methods
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251438728.PNG)  
##### constructor
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251443814.PNG)  
#### 反射爆破创建实例
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251509709.PNG)  
```java
public class Demo1 {
    public static void main(String[] args) throws Exception {
        //先获取到User类的Class对象
        Class<User> userClass = User.class;
        //通过public无参构造器创造实例
        User user = userClass.newInstance();
        //通过public有参构造器创造实例
        Constructor<User> constructor = userClass.getConstructor(String.class);
        User balance1 = constructor.newInstance("balance1");
        //非public有参构造器创造实例
        //先获取到私有构造器
        Constructor<User> declaredConstructor = userClass.getDeclaredConstructor(String.class, int.class);
        //此时还是无法直接newInstance创建，需setAccessible设置为true 爆破
        declaredConstructor.setAccessible(true);
        User balance2 = declaredConstructor.newInstance("balance2", 15);
    }
}
class User{
    private String name="balance";
    private int age=10;

    public User(){

    }
    public User(String name){
        this.name=name;
    }
    private User(String name,int age){
        this.name=name;
        this.age=age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```
#### 反射爆破操作属性
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251512518.PNG)  
```java
public class Demo1 {
    public static void main(String[] args) throws Exception {
        //获取Student Class对象
        Class<Student> studentClass = Student.class;
        Student student = studentClass.newInstance();
        System.out.println(student);
        //通过反射获得age属性对象
        Field age = studentClass.getField("age");
        //设置值
        age.set(student,88);
        System.out.println(student);
        System.out.println(age.get(student));//反射返回age值
        //通过反射操作name属性
        Field name = studentClass.getDeclaredField("name");//因为私有所以getDeclaredField
        //因为私有属性，无法直接设置，所以setAccessible(true) 爆破
        name.setAccessible(true);
        name.set(student,"balance");
        //因为name是static 也可name.set(null,"balance");
        System.out.println(student);
        System.out.println(name.get(student));//因为是static 也可name.get(null)


    }
}
class Student{
    public int age;
    private  static  String name;

    public Student() {
    }
    @Override
    public String toString() {
        return "Student{" +
                "age=" + age+" " +"name="+name+
                '}';
    }
}
```
#### 反射爆破操作方法
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310251525672.PNG)  
```java
public class Demo1 {
    public static void main(String[] args) throws Exception {
        //获取BOSS Class对象
        Class<Boss> bossClass = Boss.class;
        Boss boss = bossClass.newInstance();
        //调用public hi方法 直接getMethod就行
        Method hi = bossClass.getMethod("hi", String.class);
        hi.invoke(boss, "balance");
        //调用私用 say方法 getDeclaredMethod获取
        Method say = bossClass.getDeclaredMethod("say", int.class, String.class, char.class);
        //注意say方法无法直接调用，所以setAccessible(true) 爆破
        say.setAccessible(true);
        //因为是静态 所以直接可以传null 不是静态需传具体对象
        say.invoke(null,18,"balance",'c');
        //在反射中，如果方法有返回值，一律返回object
    }
}
class Boss{
    public int age;
    private static String name;
    public Boss(){}

    private static void say(int n,String s,char c){
        System.out.println(n+" "+s+" "+c);
    }
    public void hi(String s){
        System.out.println("hi "+s);
    }
}
```

## JUC 并发编程

JUC就是java.util.concurrent下面的类包，专门用于多线程的开发。

![image-20240327094549072](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327094549072.png)

### 线程和进程

> 进程是操作系统中的应用程序、是资源分配的基本单位，线程是用来执行具体的任务和功能，是CPU调度和分派的最小单位
>
> 一个进程往往可以包含多个线程，至少包含一个

#### 进程

**一个程序，QQ.EXE Music.EXE；**

一个进程可以包含多个线程，至少包含一个线程！

Java默认有几个线程？**2个线程！** **main线程、GC线程**

#### 线程

开了一个进程Typora，写字，等待几分钟会进行自动保存(线程负责的)

对于Java而言：Thread、Runable、Callable进行开启线程的。

**提问？JAVA真的可以开启线程吗？ 开不了的！**

Java是没有权限去开启线程、操作硬件的，这是一个native的一个本地方法，它调用的底层的C++代码。

```java
private native void start0();
```

#### 并发

多线程操作同一个资源。

- CPU 只有一核，模拟出来多条线程，天下武功，唯快不破。那么我们就可以使用CPU快速交替，来模拟多线程。
- 并发编程的本质：**充分利用CPU的资源！**

```java
public class Test1 {
    public static void main(String[] args) {
        // 获得cpu的核数 22
        // cpu密集型 IO密集型
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
```

#### 并行

**并行：** 多个人一起行走

- CPU多核，多个线程可以同时执行。 **我们可以使用线程池！**

#### 线程的状态

```java
public enum State {

    	//新建
        NEW,

    	//运行
        RUNNABLE,

    	//阻塞
        BLOCKED,

    	//等待 死死的等
        WAITING,

    	//超时等待 时间超过就不等了
        TIMED_WAITING,

    	//终止
        TERMINATED;
}
```

#### wait/sleep 区别

##### 来自不同的类

* wait => Object

* sleep => Thread

一般情况企业中使用休眠使用JUC的工具类：

```java
TimeUnit.DAYS.sleep(1); //休眠1天
TimeUnit.SECONDS.sleep(1); //休眠1s
```

##### 关于锁的释放

* wait **会释放锁**；

* sleep**睡觉了，不会释放锁；**

##### 使用的范围是不同的

* wait 必须在同步代码块中；因为是Object类的方法

* sleep 可以在任何地方睡；

### Lock

#### 传统 synchronized

独占锁 同一时刻只允许一个线程持有锁

```java
//基本的卖票例子
/*
   真正的多线程开发，即在公司中，需要降低耦合性
   线程就是一个单独的资源类，没有任何的附属操作
   不要去写一个类实现了runable接口 将线程和资源分开
 */
public class Test1 {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(()->{
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"线程A").start();
        new Thread(()->{for (int i = 0; i < 40; i++) ticket.sale();},"线程B").start();
        new Thread(()->{for (int i = 0; i < 40; i++) ticket.sale();},"线程C").start();
    }
}
//资源类
class Ticket{
    private int number = 30;

    //卖票
    public void sale(){
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票");
        }
    }
}
```

这样会导致并发问题，加上synchronized关键字可以解决

```java
//卖票
//synchronized 本质是队列，让其排队
public synchronized void sale(){
    if (number > 0) {
        System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票");
    }
}
```

#### Lock 类

`ReentrantLock`: 是一种互斥锁（Mutex Lock）（独占锁），它**只允许一个线程同时访问共享资源，其他线程必须等待当前线程释放锁后才能访问**。`ReentrantLock` 具有可重入性，同一个线程可以多次获得同一个锁。

![image-20240327102659913](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327102659913.png)

![image-20240327102807461](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327102807461.png)

```java
Lock lock = new ReentrantLock();
```

点进源码可以发现**可重入锁默认是非公平锁**

* 非公平锁：可以插队 因为有的线程执行快(3h)有的执行执行慢(3s)，不能让执行快的等待执行慢的太久
* 公平锁：先来先得



![image-20240327103114123](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327103114123.png)

```java
public class Test2 {
    public static void main(String[] args) {
        Ticket2 ticket = new Ticket2();//for循环只有一行 可以省略花括号
        new Thread(()->{for (int i = 0; i < 40; i++) ticket.sale();},"线程A").start();
        new Thread(()->{for (int i = 0; i < 40; i++) ticket.sale();},"线程B").start();
        new Thread(()->{for (int i = 0; i < 40; i++) ticket.sale();},"线程C").start();
    }
}
// Lock
class Ticket2{
    private int number = 30;
    Lock lock = new ReentrantLock();//新建一个锁

    //卖票
    public  void sale(){
        try {
            lock.lock();//加锁
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出了第" + (number--) + "张票");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
    }
}
```

#### 两者区别

1. Synchronized 内置的**Java关键字**，Lock是一个**Java类**

2. Synchronized 无法判断锁是否已经被其他线程占用，Lock可以判断 trylock 返回true和false

3. Synchronized 会自动释放锁，**lock必须要手动加锁和手动释放锁**！可能会遇到死锁

4. Synchronized 线程1(获得锁->阻塞)、线程2(等待)；**lock就不一定会一直等待下去，lock会有一个trylock去尝试获取锁，不会造成长久的等待。**

5. Synchronized 是**可重入锁，不可以中断的，非公平的**；Lock，**可重入的，可以判断锁，可以自己设置公平锁和非公平锁；**

6. **Synchronized 适合锁少量的代码同步问题，Lock适合锁大量的同步代码**；

### 生产者和消费者问题

> 面试必会 单例模式 排序算法 生产者和消费者 死锁

Synchronized 版    wait notify

JUC Lock解决

#### Synchronzied 版本

```java
/*
    线程之间的通信问题：生产者消费者 等待唤醒 通知唤醒 问题
    线程交替执行 A B 操作同一个变量 num=0
    A num++
    B num--
*/
public class Test3_Pro_Con {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(()->{for (int i = 0; i < 5; i++) data.increment();},"A").start();
        new Thread(()->{for (int i = 0; i < 5; i++) data.decrement();},"B").start();
    }
}

//资源类 数字
class Data{
    private int number=0;

    //+1
    public synchronized void increment(){
        if(number!=0){
            try {
                this.wait(); //等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number++; //+1
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        this.notifyAll();//通知其它线程 我+1完毕了 你需要-1
    }
    //+1
    public synchronized void decrement(){
        if(number==0){
            try {
                this.wait();//等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        number--;//-1
        System.out.println(Thread.currentThread().getName()+"\t"+number);
        this.notifyAll();//通知其它线程 我-1完毕了 你需要加+1
    }
}
```

**现在只有两个线程，但线程更多的时候还安全吗？**

![image-20240327110511112](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327110511112.png)

![image-20240327110617094](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327110617094.png)

```java
public synchronized void increment(){
    while(number!=0){//改为while
        try {
            this.wait(); //等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    number++; //+1
    System.out.println(Thread.currentThread().getName()+"\t"+number);
    this.notifyAll();//通知其它线程 我+1完毕了 你需要-1
}
//+1
public synchronized void decrement(){
    while(number==0){//改为while
        try {
            this.wait();//等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    number--;//-1
    System.out.println(Thread.currentThread().getName()+"\t"+number);
    this.notifyAll();//通知其它线程 我-1完毕了 你需要加+1
}
```

当使用 if 进行判断只会判断一次，如果有一个增加的线程进入，发现当前为0，执行了+1操作，另一个增加的线程进入 当前不为0 所以等待，但前面的线程+1之后唤醒其它线程，此时另一个增加的线程就不会再次进行判断而直接+1。

#### JUC版生产者和消费者问题

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/dad7044c4b8b46648084823841cb6781.png)

![image-20240327130102209](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327130102209.png)

```java
class Data{
    private int number=0;
    Lock lock = new ReentrantLock();//默认访问修饰符，默认情况下可被同一包中的其他类访问
    Condition condition = lock.newCondition();

    //+1
    public void increment() throws InterruptedException {
        lock.lock();//加锁
        try {
            //业务代码
            while(number!=0){
                condition.await();//等待
            }
            number++; //+1
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            condition.signalAll();//通知
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();//解锁
        }
    }
    //+1
    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            while(number==0){
                condition.await();
            }
            number--;//-1
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
```

可以发现正常执行了。

**但是思考一个问题，这种解决方式和之前的效果相同，为什么要引入新的呢？**

因为Condition 可以精准的通知和唤醒线程

![image-20240327131530195](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327131530195.png)

````java
public class Test04_Condition {
    public static void main(String[] args) {
        Data4 data = new Data4();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printA();
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printB();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.printC();
            }
        },"C").start();
    }
}

class Data4{
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();
    private int number = 1; //1A 2B 3C

    public void printA() {
        lock.lock();
        try {
            while(number!=1){
                condition1.await();
            }
            number=2;
            condition2.signal();//唤醒 2
            System.out.println("线程："+Thread.currentThread().getName()+"\t"+"AAAAAAAAAAAA");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void printB(){
        lock.lock();
        try {
            while(number!=2){
                condition2.await();
            }
            number=3;
            condition3.signal();//唤醒 3
            System.out.println("线程:"+Thread.currentThread().getName()+"\t"+"BBBBBBBBBBBB");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void printC(){
        lock.lock();
        try {
            while(number!=3){
                condition3.await();
            }
            number=1;
            condition1.signal();//唤醒 1
            System.out.println("线程:"+Thread.currentThread().getName()+"\t"+"CCCCCCCCCCCCCC");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
````

![image-20240327133243863](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240327133243863.png)

> synchronized的await notify、Lock的Condition.await signal以及LockSupport的park unpark 三种方式的区别？

1. **`synchronized`的`wait`/`notify`**：这是Java最基本的线程同步机制。当一个线程调用了一个对象的`wait`方法后，它会释放对该对象的锁，然后进入等待状态，直到其他线程调用同一个对象的`notify`或`notifyAll`方法。**`wait`/`notify`方法必须在`synchronized`块或方法中使用**，否则会抛出`IllegalMonitorStateException`。
2. **`Lock`的`Condition.await`/`signal`**：这是一个更灵活的线程同步机制。与`synchronized`的`wait`/`notify`相比，`Condition`提供了更多的功能，例如可以有多个等待队列（每个`Condition`对象对应一个等待队列），可以选择性地唤醒等待队列中的线程等。**`await`/`signal`方法必须在`Lock.lock`和`Lock.unlock`方法中使用**，否则会抛出`IllegalMonitorStateException`。
3. **`LockSupport.park`/`unpark`**：这是一个最底层的线程阻塞机制。**与上面两种方式相比，`LockSupport`的优点是它不需要获取对象的锁或者`Lock`对象**，可以直接阻塞或唤醒任何线程。此外，`unpark`方法可以在`park`方法之前调用，这使得`LockSupport`可以避免一些因为`wait`/`notify`或`await`/`signal`的调用顺序不正确而导致的问题。

### 八锁现象

如何判断锁的是谁！锁到底锁的是谁？

锁会锁住：对象、Class

深刻理解我们的锁

#### 问题1

两个同步方法，先执行发短信还是打电话

```java
public class dome01 {
    public static void main(String[] args) {
        Phone phone = new Phone();

        new Thread(() -> { phone.sendMs(); }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() {
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
}
```

输出结果为

发短信

打电话

**为什么？ 如果你认为是打电话顺序在前？ 这个答案是错误的！**

#### 问题2

**我们再来看：我们让发短信 延迟4s**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
}
```

现在结果是什么呢？

结果：**还是先发短信，然后再打电话！**

**why？**

> 原因：并不是顺序执行，而是synchronized 锁住的对象是方法的调用！对于两个方法用的是同一个锁，谁先拿到谁先执行，另外一个等待

#### **问题三**

加一个普通方法

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();

        new Thread(() -> {
            try {
                phone.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone.hello(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果为

hello

发短信

> 原因：hello是一个普通方法，不受synchronized锁的影响，不用等待锁的释放

#### **问题四**

**如果我们使用的是两个对象，一个调用发短信，一个调用打电话，那么整个顺序是怎么样的呢？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone2.call(); }).start();
    }
}

class Phone {
    public synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：两个对象两把锁，不会出现等待的情况，发短信睡了4s,所以先执行打电话

#### 问题五、六

如果我们把synchronized的方法加上static变成静态方法！那么顺序又是怎么样的呢？

（1）我们先来使用一个对象调用两个方法！

答案是：先发短信,后打电话

（2）如果我们使用两个对象调用两个方法！

答案是：还是先发短信，后打电话

原因是什么呢？ 为什么加了static就始终前面一个对象先执行呢！为什么后面会等待呢？

原因是：对于static静态方法来说，对于整个类Class来说只有一份，对于不同的对象使用的是同一份方法，相当于这个方法是属于这个类的，如果静态static方法使用synchronized锁定，那么这个synchronized锁会锁住整个对象！不管多少个对象，对于静态的锁都只有一把锁，谁先拿到这个锁就先执行，其他的进程都需要等待！

#### **问题七**

**如果我们使用一个静态同步方法、一个同步方法、一个对象调用顺序是什么？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
//        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone1.call(); }).start();
    }
}

class Phone {
    public static synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：因为一个锁的是Class类的模板，一个锁的是对象的调用者。所以不存在等待，直接运行。

#### **问题八**

**如果我们使用一个静态同步方法、一个同步方法、两个对象调用顺序是什么？**

```java
public class dome01 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendMs();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> { phone2.call(); }).start();
    }
}

class Phone {
    public static synchronized void sendMs() throws InterruptedException {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("发短信");
    }
    public synchronized void call() {
        System.out.println("打电话");
    }
    public void hello() {
        System.out.println("hello");
    }
}
```

输出结果

打电话

发短信

> 原因：两把锁锁的不是同一个东西

#### 总结

**new** 出来的 this 是具体的一个对象

**static Class** 是唯一的一个模板

### 集合不安全

#### List不安全

```java
//java.util.ConcurrentModificationException 并发修改异常！
public class Test05_List {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
```

![image-20240328101144195](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240328101144195.png)

如何修改使之安全呢？

* Vector 遍历操作消耗高
* Collections.synchronizedList(new ArrayList<>()) 读写性能和 vector差不多，但可以包装多种LIst，兼容性强

* new CopyOnWriteArrayList<>() 写操作性能消耗高

```java
public boolean add(E e) {
        final ReentrantLock lock = this.lock;//用LOck而非sync关键字
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);//复制
            newElements[len] = e;
            setArray(newElements);//插入
            return true;
        } finally {
            lock.unlock();
        }
    }
```

Vector:

- Vector 是 Java 中最早的线程安全的动态数组实现之一。

- 它的所有方法都是同步的，这意味着每个方法在执行时都会获取对象级别的锁，以确保线程安全性。
- 尽管它是线程安全的，但由于**所有方法都是同步的，因此在并发访问高的情况下性能可能会受到影响**。
- 由于性能问题，一般**不推荐在新的代码中使用 Vector**。

Collections.synchronizedList(new ArrayList<>()):

- Collections.synchronizedList() 方法返回一个由指定列表支持的同步（线程安全的）列表。

- 它的实现方式是通过在每个方法调用时使用列表对象本身作为锁来实现同步。
- 这种方式**相对于 Vector 更加灵活，因为你可以选择任何列表实现**，而不仅仅是 Vector。
- 与 Vector 相似，由于方法级别的同步，性能可能受到影响，尤其是在高并发场景下。

new CopyOnWriteArrayList<>():**读写分离**

- CopyOnWriteArrayList 是 Java 并发包（java.util.concurrent）中提供的一种线程安全的列表实现。
- 它的实现方式是通过使用一种称为**“写时复制”（Copy-On-Write）的机制**，即在修改列表时不直接修改原列表，而是创建一个新的副本，然后将修改应用到副本上，最后将副本替换原列表。
- 由于读操作不涉及同步，因此读取性能很高，**适用于读多写少的场景**。
- 由于每次修改都会创建一个副本，因此写操作的性能相对较低，适用于写少读多的场景。

**CopyOnWriteArrayList：**写入时复制！ COW 计算机程序设计领域的一种优化策略

核心思想是，如果有多个调用者（Callers）同时要求相同的资源（如内存或者是磁盘上的数据存储），他们会共同获取相同的指针指向相同的资源，**直到某个调用者视图修改资源内容时，系统才会真正复制一份专用副本**（private copy）给该调用者，而其他调用者所见到的最初的资源仍然保持不变。这过程对其他的调用者都是透明的（transparently）。此做法主要的优点是如果调用者没有修改资源，就不会有副本（private copy）被创建，因此多个调用者只是读取操作时可以共享同一份资源。

#### Set不安全

**Set和List同理可得:** 多线程情况下，普通的Set集合是线程不安全的；

解决方案还是两种：

- 使用Collections工具类的**synchronized**包装的Set类
- 使用CopyOnWriteArraySet 写入复制的**JUC**解决方案

![image-20240328103233998](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240328103233998.png)

```java
public class Test06_set {
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        //Set<String> set1 = Collections.synchronizedSet(set); //方式一
        Set<String> set1 = new CopyOnWriteArraySet<>();//方式二
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                set1.add(UUID.randomUUID().toString().substring(0,5));
                System.out.println(set1);
            },String.valueOf(i)).start();
        }
    }
}
```

#### map不安全

hashSet底层就是一个**HashMap**；

![image-20240328104052031](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240328104052031.png)

```java
public class Test07_Map {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();//方式一
        //Map<Object, Object> map1 = Collections.synchronizedMap(new HashMap<>()); 方式二
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
```

**HashMap**:

- `HashMap` 是 Java 中最常用的哈希表实现之一，它是非线程安全的。
- 在单线程环境下，`HashMap` 提供了较高的性能，因为它不涉及额外的同步开销。
- 在多线程环境下，如果多个线程同时访问或修改同一个 `HashMap` 实例，可能会导致不确定的结果，甚至抛出 `ConcurrentModificationException` 异常。

**ConcurrentHashMap**:

- `ConcurrentHashMap` 是 Java 并发包中提供的线程安全的哈希表实现。
- JDK1.7底层采用分段的数组+链表实现
- JDK1.8采用的数据结构跟HashMap1.8的结构一样，数组+链表/红黑二叉树
- 加锁的方式
  - JDK1.7采用Segment分段锁，底层使用的是ReentrantLock
  - **JDK1.8采用CAS添加新节点，采用synchronized锁定链表或红黑二叉树的首节点，相对Segment分段锁粒度更细，性能更好**
  - 在`ConcurrentHashMap`中，每个键值对都是一个独立的节点（Node），每个节点都可以被CAS操作独立地更新。


### callable

与 `Runnable` 接口类似，`Callable` 接口也表示一个可以在单独线程中执行的任务，但是 `Callable` 接口与 `Runnable` 接口不同的是，**`Callable` 接口的 `call()` 方法可以返回一个结果，而且可以抛出检查异常**。

![image-20240328110630771](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240328110630771.png)

```java
public class Test08_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyThread myThread = new MyThread();
        //线程只能通过 new Thread().start(); 启动
        //但Thread构造方法需要一个Runnable 如何解决？
        //Runnable 有一个实现类 FutureTask ，它的构造方法可以接受一个Callable
        FutureTask<String> futureTask = new FutureTask<>(myThread);
        new Thread(futureTask, "线程1").start();
        new Thread(futureTask, "线程2").start();//内部有一个state 如果不为new，就直接返回，我们只创建了一个FutureTask对象，故只打印一次
        //打印返回值
        String s = futureTask.get();//使用get方法获得执行后的返回值
        System.out.println(s);// 注意get方法有可能阻塞住，比如中间有一些耗时操作，一般将其调用get方法放在最后
    }
}
class MyThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("callable");
        //中间可能有耗时操作产生阻塞
        return "1024";
    }
}
```

### 常用辅助类

#### CountDownLatch 减法

`CountDownLatch` 是 Java 中的一个同步辅助类，它可以让一个或多个线程等待其他线程完成一组操作后再继续执行。

```java
public class Test09_CountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t离开教室");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println("close the door");
    }
}
```

![image-20240328122837998](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240328122837998.png)

主要方法：

- countDown 减一操作；
- await 等待计数器归零

await 等待计数器归零，归零就唤醒，再继续向下运行

#### CyclicBarrier 加法

`CyclicBarrier` 是 Java 中的一个同步辅助类，**它允许一组线程互相等待，直到到达某个公共的屏障点（barrier），然后继续执行。**一旦所有线程都到达屏障点，**屏障点上的任务（通过 `CyclicBarrier` 构造函数中的可选的 `Runnable` 参数指定）将被执行，并且所有线程将继续执行。**

`CyclicBarrier` 主要包含两个方法：`await()` 和 `reset()`。

- `await()` 方法用于使当前线程等待，直到所有参与者线程都到达屏障点。
- `reset()` 方法用于重置屏障点，以便 `CyclicBarrier` 可以被重用。

```java
public class Test10_CyclicBarrier {
    public static void main(String[] args) {
        //当等待的线程数达到7时，执行集齐七颗龙珠
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> System.out.println("集齐七颗龙珠召唤神龙"));
        for (int i = 0; i < 7; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    cyclicBarrier.await();//+1
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}
```

#### Semaphore

`Semaphore` 是 Java 中的一个同步辅助类，它**用于控制对共享资源的访问**。`Semaphore` 维护了一组许可证（permits），**线程在访问共享资源之前必须先获取许可证，访问完成后再释放许可证。如果许可证数已经用完，其他线程必须等待直到有线程释放许可证。**

`Semaphore` 主要包含两个方法：`acquire()` 和 `release()`。

- `acquire()` 方法用于获取一个许可证，如果当前没有可用的许可证，则线程会被阻塞，直到有可用的许可证。
- `release()` 方法用于释放一个许可证，使其可供其他等待的线程使用。

`Semaphore` 的典型用法是控制对共享资源的并发访问数量。例如，可以用 `Semaphore` 来限制同时访问某个文件的线程数量，或者限制同时执行某个任务的线程数量

**所以，即使Semaphore的初始许可数为0，也可以通过调用release()方法来增加许可数。这是Semaphore用于同步控制的一种机制。**可以利用此控制线程的执行顺序

```java
public class Test11_Semaphore {
    public static void main(String[] args) {
        //一共有三个车位
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();//得到车位
                    System.out.println(Thread.currentThread().getName() + "抢到车位");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + "离开车位");
                    semaphore.release();//释放车位
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}
```

`Semaphore` 和互斥锁（Mutex）区别

- **Semaphore**: 主要用于控制对共享资源的访问数量，**它允许多个线程同时访问共享资源，但是可以限制同时访问的线程数量。**
- **互斥锁**: 主要用于保护临界区，它**只允许一个线程访问共享资源，其他线程必须等待当前线程释放锁后才能访问。**

### ReadWriteLock

`ReadWriteLock` 是 Java 中用于支持读写分离的同步工具，**它允许多个线程同时读取共享资源，但只允许一个线程写入共享资源**。读写锁提供了比传统的互斥锁更高的并发性，在读多写少的场景下可以提升性能。

我们希望**写入时只允许一个线程写入，而读取时可以多个线程同时读取**

```java
public class Test12_ReadWriteLock {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 5; i++) {
            int j = i;
            new Thread(() -> {
                myCache.put("key" + j, "value" + j);
            },"Thread"+i).start();
        }
        for (int i = 0; i < 5; i++) {
            int j = i;
            new Thread(() -> {
                myCache.get("key" + j);
            },"Thread"+i).start();
        }
    }
}
class MyCache{
    private volatile Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value){
        System.out.println(Thread.currentThread().getName()+" 开始写入");
        map.put(key, value);
        System.out.println(Thread.currentThread().getName()+" 写入完成");
    }
    public void get(String key){
        System.out.println(Thread.currentThread().getName()+" 开始读取");
        map.get(key);
        System.out.println(Thread.currentThread().getName()+" 读取完成");
    }
}
```

```
0写入开始
1写入开始
4写入开始
0写入结束
```

不满足需求

使用读写锁更改

```java
public class Test12_ReadWriteLock {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.write(tempInt + "", tempInt + "");
            }, String.valueOf(i)).start();
        }
        for (int i = 0; i < 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.read(tempInt + "");
            }, String.valueOf(i)).start();
        }
    }
}
class MyCache{
    private volatile Map<String,String> map=new HashMap<>();
    private ReadWriteLock readWriteLock=new ReentrantReadWriteLock();

    public void write(String key,String value){
        readWriteLock.writeLock().lock();//加写锁
        try {
            System.out.println(Thread.currentThread().getName()+"写入开始");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName()+"写入结束");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.writeLock().unlock();//解锁
        }

    }
    public void read(String key){
        readWriteLock.readLock().lock();//加读锁
        try {
            System.out.println(Thread.currentThread().getName()+"读取开始");
            map.get(key);
            System.out.println(Thread.currentThread().getName()+"读取结束");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            readWriteLock.readLock().unlock();//解锁
        }
    }
}
```

```
1写入开始
1写入结束
2写入开始
2写入结束
0写入开始
0写入结束
3写入开始
3写入结束
4写入开始
4写入结束
4读取开始
0读取开始
4读取结束
2读取开始
2读取结束
3读取开始
3读取结束
1读取开始
1读取结束
0读取结束
```

满足要求

### 阻塞队列

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/3b6b0b33e6e9b0f2261a89b6e42e78ea.png)

#### BlockQueue

是Collection的一个子类

什么情况下我们会使用阻塞队列

> 多线程并发处理、线程池

![image-20240329100307617](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329100307617.png)

**BlockingQueue 有四组api**

| 方式         | 抛出异常 | 不会抛出异常，有返回值 | 阻塞、等待 | 超时等待                |
| ------------ | -------- | ---------------------- | ---------- | ----------------------- |
| 添加         | add      | offer                  | put        | offer(timenum.timeUnit) |
| 移出         | remove   | poll                   | take       | poll(timenum,timeUnit)  |
| 判断队首元素 | element  | peek                   | -          |                         |

```java
public class Test13_BlockQueue {
    public static void main(String[] args) {

        try {
            test5();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
    //-------------------------add/remove---------------------------------------
    public static void test1(){
        //参数为需要初始化队列的大小
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.add("a"));//add 返回添加成功 若超出容量，抛出异常
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
       // System.out.println(blockingQueue.add("d"));//java.lang.IllegalStateException: Queue full

        System.out.println(blockingQueue.remove());//remove 返回移除的元素 若队列为空，抛出异常
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        //System.out.println(blockingQueue.remove());//java.util.NoSuchElementException
    }
    //-------------------------offer/poll---------------------------------------
    public static void test2(){
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a"));//offer 返回添加成功与否  若超出容量，返回false
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        //System.out.println(blockingQueue.offer("d"));//false

        System.out.println(blockingQueue.poll());//poll 返回移除的元素 若队列为空，返回null
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        //System.out.println(blockingQueue.poll());//null
    }
    //-------------------------put/take---------------------------------------
    public static void test3() throws InterruptedException {
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.put("a");//put 不返回值 若超出容量阻塞在那里
        blockingQueue.put("b");
        blockingQueue.put("c");
        //blockingQueue.put("d");程序阻塞
        System.out.println(blockingQueue.take());//take 返回取出的元素 若无元素可取则阻塞
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        //System.out.println(blockingQueue.take());//程序阻塞
    }
    //-------------------------offer/poll超时等待---------------------------------------
    public static void test4() throws InterruptedException {
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println(blockingQueue.offer("a",2, TimeUnit.SECONDS));//offer超时等待，若插入不成功，等待指定时间，若还不成功 返回false
        System.out.println(blockingQueue.offer("b",2, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c",2, TimeUnit.SECONDS));
        //System.out.println(blockingQueue.offer("d",2, TimeUnit.SECONDS));//false

        System.out.println(blockingQueue.poll(2, TimeUnit.SECONDS));//poll超时等待，若取不成功，等待指定时间，若还不成功 返回null
        System.out.println(blockingQueue.poll(2, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(2, TimeUnit.SECONDS));
        //System.out.println(blockingQueue.poll(2, TimeUnit.SECONDS));//null
    }
    //-------------------------element 异常/peek 返回值 队首元素---------------------------------------
    public static void test5(){
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(3);
        // java.lang.RuntimeException: java.util.NoSuchElementException
        //System.out.println(blockingQueue.element());//element 返回队首元素 若队列为空，抛出异常 

        System.out.println(blockingQueue.peek());//peek 返回队首元素 若队列为空，返回null
    }

}
```

#### SynchronousQueue同步队列

`SynchronousQueue` 是 Java 并发包（`java.util.concurrent`）中的一种特殊类型的队列，**它在生产者和消费者之间传递元素，但其内部没有容量。也就是说，生产者线程往队列中添加元素时，必须等待消费者线程来获取这个元素；反之，消费者线程在尝试获取元素时，必须等待生产者线程往队列中添加元素。**因此，`SynchronousQueue` 在内部没有任何容量，**每个插入操作必须等待一个对应的移除操作**，反之亦然。

```java
public class Test14_SynchronousQueue {
    public static void main(String[] args) {
        test();
    }
    public static void test() {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();//同步队列
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " put a");
                synchronousQueue.put("a");
                System.out.println(Thread.currentThread().getName() + " put b");
                synchronousQueue.put("b");
                System.out.println(Thread.currentThread().getName() + " put c");
                synchronousQueue.put("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " take a");
                synchronousQueue.take();
                System.out.println(Thread.currentThread().getName() + " take b");
                synchronousQueue.take();
                System.out.println(Thread.currentThread().getName() + " take c");
                synchronousQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
```

### 线程池

线程池：三大方法、七大参数、四种拒绝策略

> 池化技术

程序的运行，本质：占用系统的资源！我们需要去优化资源的使用 ===> 池化技术

线程池、JDBC的连接池、内存池、对象池 等等。。。。

**资源的创建、销毁十分消耗资源**

**池化技术**：事先准备好一些资源，如果有人要用，就来我这里拿，用完之后还给我，以此来提高效率。

#### 线程池的好处

1. **减少资源消耗**：线程池中的线程可以被重复利用，避免了频繁地创建和销毁线程的开销，从而减少了系统资源的消耗。
2. **提高响应速度**：通过线程池，可以预先创建一定数量的线程，当有任务到达时，可以立即分配线程来处理，不需要等待新线程的创建，从而提高了任务的响应速度。
3. **提高系统稳定性**：合理地配置线程池的大小可以控制系统中并发线程的数量，避免因创建过多线程而导致系统资源不足或系统负载过高，从而提高了系统的稳定性。
4. **方便管理和调优**：线程池提供了一系列管理和监控线程的方法，可以方便地对线程池进行监控、调优和管理，例如设置线程池的大小、超时时间、拒绝策略等。
5. **支持任务排队**：线程池可以通过任务队列来存储等待执行的任务，当线程池中的线程达到上限时，新的任务可以被放入队列中等待执行，从而避免了任务的丢失和阻塞。
6. **控制并发度**：通过合理地设置线程池的大小和任务队列的容量，可以灵活控制系统的并发度，避免因并发量过高而导致系统崩溃或性能下降。

==线程复用、可以控制最大并发数、管理线程==

#### 三大方法-常用

这些方法创建的线程池中的线程是守护线程，如果主线程退出，这些线程也会被自动销毁。如果你希望主线程退出后，线程池中的线程仍然执行，你需要手动创建非守护线程，并将它们添加到线程池中。

```java
public class Demo01_ThreeFunc {
    public static void main(String[] args) {
        //ExecutorService executorService = Executors.newSingleThreadExecutor();//单个线程
        //ExecutorService executorService = Executors.newFixedThreadPool(5);//多个线程，固定大小的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();//根据任务所需自动调整线程池大小
        try {
            for (int i = 0; i < 100; i++) {
                executorService.execute(()-> System.out.println(Thread.currentThread().getName()+"ok"));
            }
        } finally {
            executorService.shutdown();
        }
    }
}
```

实际上还有两个方法

* newWorkStealingPool  底层是 forkjoinpool

* newScheduledThreadPool 底层用了 DelayedWorkQueue

  ```java
  super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
                new DelayedWorkQueue());
  ```

![image-20240427162238339](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240427162238339.png)

#### 七大参数

```java
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService
        (new ThreadPoolExecutor(1, 1,
                                0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>()));
}
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>());
}

public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                  60L, TimeUnit.SECONDS,
                                  new SynchronousQueue<Runnable>());
}
// 三个方法的底层都是 ThreadPoolExecutor

// ThreadPoolExecutor
public ThreadPoolExecutor(int corePoolSize,//核心线程数
                          int maximumPoolSize,//最大线程数
                          long keepAliveTime,//超时等待
                          TimeUnit unit,//时间单位
                          BlockingQueue<Runnable> workQueue) {//阻塞队列
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), defaultHandler);
}        //线程工厂                         //拒绝策略
```

底层都是通过 new ThreadPoolExecutor去创建的，该类接收7个参数去创建

![image-20240329125209117](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329125209117.png)

![image-20240329123345076](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329123345076.png)

**当程序尝试分配更多的内存，但系统的可用内存已经不足时，就会导致OOM错误(Out of Memory 内存耗尽或内存溢出)**

```java
public class Demo02_SevenParm {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 100; i++) {
                threadPoolExecutor.execute(()-> System.out.println(Thread.currentThread().getName()+"ok"));
            }
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

}
```

#### 四大拒绝策略

- **new ThreadPoolExecutor.AbortPolicy()**： //该拒绝策略为：银行满了，还有人进来，不处理这个人的，并抛出异常，超出最大承载，就会抛出异常：队列容量大小+maxPoolSize
- **new ThreadPoolExecutor.CallerRunsPolicy()**： //该拒绝策略为：哪来的去哪里 main线程进行处理
- **new ThreadPoolExecutor.DiscardPolicy():** //该拒绝策略为：队列满了,丢掉任务，不会抛出异常。
- **new ThreadPoolExecutor.DiscardOldestPolicy()**： //该拒绝策略为：队列满了，尝试去和最早的线程竞争，不会抛出异常

#### 小结及拓展

线程池大小如何设置？

**CPU密集型**：电脑的核数是几核就选择几；选择maximunPoolSize的大小

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
    2,
    Runtime.getRuntime().availableProcessors(),//代码动态获取
    3,
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(3),
    Executors.defaultThreadFactory(),
    new ThreadPoolExecutor.AbortPolicy());
```

**I/O密集型**：在程序中有15个大型任务，io十分占用资源；

I/O密集型就是判断我们程序中十分耗I/O的线程数量，大约是最大I/O数的一倍到两倍之间。

### 四大函数式接口

**lambda表达式、链式编程、函数式接口、Stream流式计算**

**函数式接口**：只有一个抽象方法的接口 例如Runable

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
//foreach方法 参数消费者类型的函数式接口
```

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/a4bd596dc9a77d9da8821dd6312a4314.png)

 

#### Function 函数型接口

![image-20240329132200404](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329132200404.png)

```java
public class Demo01_Function {
    public static void main(String[] args) {
        Function<String, String> function = str->str;//(str)->{return str;}
        System.out.println(function.apply("helloworld"));
    }
}
```

#### Predicate 断言型接口

![image-20240329132832165](C:\Users\18356\AppData\Roaming\Typora\typora-user-images\image-20240329132832165.png)

```java
public class Demo02_Predicate {
    public static void main(String[] args) {
        Predicate<String> predicate=str->str.isEmpty();
        System.out.println(predicate.test("444"));//传入不为空时返回true，否则返回false
    }
}
```

#### Suppier 供给型接口

**无参数**

![image-20240329133230941](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329133230941.png)

```java
public class Demo04_Supplier {
    public static void main(String[] args) {
        Supplier<Integer> supplier=()->{return 10;};
        System.out.println(supplier.get());
    }
}
```

#### Consummer 消费型接口

无返回值

![image-20240329133147127](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240329133147127.png)

```java
public class Demo03_Consumer {
    public static void main(String[] args) {
        Consumer<String> consumer= System.out::println;

        consumer.accept("hello world");
    }
}
```

### Stream 流式运算

> 什么是 Stream 流式计算 

大数据：存储（集合/mysql）+计算（Stream ）

```java
/**
 * Description：
 * 题目要求： 用一行代码实现
 * 1. Id 必须是偶数
 * 2.年龄必须大于23
 * 3. 用户名转为大写
 * 4. 用户名倒序
 * 5. 只能输出一个用户
**/

public class StreamDemo {
    public static void main(String[] args) {
        User u1 = new User(1, "a", 23);
        User u2 = new User(2, "b", 23);
        User u3 = new User(3, "c", 23);
        User u4 = new User(6, "d", 24);
        User u5 = new User(4, "e", 25);

        List<User> list = Arrays.asList(u1, u2, u3, u4, u5);
        // lambda、链式编程、函数式接口、流式计算
        list.stream()
                .filter(user -> {return user.getId()%2 == 0;})
                .filter(user -> {return user.getAge() > 23;})
                .map(user -> {return user.getName().toUpperCase();})
                .sorted((user1, user2) -> {return user2.compareTo(user1);})
                .limit(1)
                .forEach(System.out::println);
    }
}
```

### ForkJoin 分支合并

**大数据中，并行执行任务，提高效率**

大数据：Map Reduce （把大任务拆分成小任务，用不同的线程并且去处理，最终合并结果）

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/c2acd43fd3363e780aa9709a6fae2d8c.png)

#### **ForkJoin的特点：工作窃取**

实现原理是：**双端队列**！从上面和下面都可以去拿到任务进行执行！

![image-20200812163701588](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/7ccffb99e41ec5a89ef2118cf0c4f0f2.png)

####  如何使用ForkJoin

- 1、通过**ForkJoinPool**来执行
- 2、计算任务 **execute(ForkJoinTask<?> task)**
- 3、计算类要去继承ForkJoinTask；

![image-20240330132236977](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240330132236977.png)

一个任务的具体实现

```java
public class ForkJoinDemo extends RecursiveTask<Long> {
    private long start;
    private long end;

    //临界值，大于这个值就使用ForkJoin
    private long THRESHOLD = 10000L;

    public ForkJoinDemo(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 任务的具体计算
     *
     * @return the result of the computation
     */
    @Override
    protected Long compute() {
        if(end - start < THRESHOLD){
            long sum = 0L;
            for(long i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        }else{
            long middle = start+(end-start)/2;//防止溢出
            ForkJoinDemo left = new ForkJoinDemo(start, middle);
            ForkJoinDemo right = new ForkJoinDemo(middle + 1, end);
            left.fork();//拆分任务，将任务压入线程队列
            right.fork();//拆分任务，把任务压入线程队列
            return left.join() + right.join();//获得合并结果
        }
    }
}
```

```java
public class Test_ForkJoin {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test1();
        test2();
        test3();
    }

    public static void test1() {
        long startTime=System.currentTimeMillis();
        long sum=0;
        for(long i=1;i<=20_0000_0000L;i++) {
            sum+=i;
        }
        long endTime=System.currentTimeMillis();
        System.out.println(sum+"       "+"耗时"+(endTime-startTime)+"毫秒");
        System.out.println("------------------------------------");
    }

    public static void test2() throws ExecutionException, InterruptedException {
        long startTime=System.currentTimeMillis();

        //使用forkjoin
        ForkJoinPool forkJoinPool = new ForkJoinPool();//新建 forkjoinpool
        ForkJoinDemo forkJoinDemo = new ForkJoinDemo(0, 20_0000_0000L); //新建任务
        ForkJoinTask<Long> submit = forkJoinPool.submit(forkJoinDemo);//提交任务
        long sum = submit.get(); //获取结果

        long endTime=System.currentTimeMillis();
        System.out.println(sum+"       "+"耗时"+(endTime-startTime)+"毫秒");
        System.out.println("------------------------------------");
    }
    public static void test3() {
        long startTime=System.currentTimeMillis();
        long sum= LongStream.rangeClosed(0,20_0000_0000L).parallel().reduce(0,Long::sum);
        long endTime=System.currentTimeMillis();
        System.out.println(sum+"       "+"耗时"+(endTime-startTime)+"毫秒");
        System.out.println("------------------------------------");
    }
}
```

```
2000000001000000000       耗时485毫秒
------------------------------------
2000000001000000000       耗时163毫秒
------------------------------------
2000000001000000000       耗时214毫秒
------------------------------------
```

当数据量不是特别大时，并行流不一定比forkjoin快

### 异步回调

> Future 设计的初衷：对将来的某个事件结果进行建模！

前端的ajax

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/9f7114b8ef92f8bd5889d865ccf4707a.png)

平时都使用**CompletableFuture**

#### 没有返回值的runAsync异步回调

```java
public class Test_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 模拟一个耗时的操作，例如读取数据库或调用远程服务
            try {
                TimeUnit.SECONDS.sleep(2);// 假设耗时2秒
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "runAsync=>Void");
        });
        // 主线程可以继续执行其他任务
        System.out.println("11111");
        // 等待异步任务完成
        completableFuture.get();
    }
}
```

#### 有返回值的异步回调supplyAsync

```java
CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
    try {
        TimeUnit.SECONDS.sleep(2);// 假设耗时2秒
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    System.out.println(Thread.currentThread().getName() + "   supplyAsync=>Integer");
    return 200;
});
System.out.println(completableFuture.whenComplete((t,u)->{
    System.out.println("t=>"+t);//正常的返回结果
    System.out.println("u=>"+u);//抛出错误的信息
}).exceptionally((e)->{
    System.out.println(e.getMessage());
    return 404;
}).get());
```

**whenComplete**: 有两个参数，一个是t 一个是u

T：是代表的 **正常返回的结果**；

U：是代表的 **抛出异常的错误信息**；

如果发生了异常，get可以获取到**exceptionally**返回的值；

### JMM

`volatile` 是 Java 中的一个关键字，它的主要作用包括：

1. **可见性**：当一个变量被 `volatile` 关键字修饰时，当一个线程修改了这个变量的值后，其他线程能够立即看到修改后的值。**这是因为 `volatile` 修饰的变量会被直接写入主内存，而不是线程的本地内存中，从而保证了所有线程对该变量的可见性**。
2. **禁止指令重排序**：`volatile` 关键字还可以禁止指令重排序优化。在 Java 内存模型中，编译器和处理器会对指令进行优化重排序，但对于被 `volatile` 修饰的变量，编译器和处理器会禁止对其进行重排序，从而确保了程序的正确性。
3. **不保证原子性**：`volatile` 关键字本身并不具备原子性，它**只能保证对于单个的读/写操作具备原子性。但是对于多个操作组合起来的复合操作，`volatile` 无法保证其原子性**，仍需要使用 `synchronized` 或 `Lock` 等机制来保证。

==JMM：JAVA内存模型，不存在的东西，是一个概念，也是一个约定！==

**关于JMM的一些同步的约定：**

- 线程解锁前，必须把共享变量**立刻**刷回主存；
- 线程加锁前，必须**读取主存**中的最新值到工作内存中；
- 加锁和解锁是同一把锁；

线程修改的东西实际上是主内存中复制到线程工作内存中的值，故当解锁后，需要将共享变量刷回去。

**8种操作:**

- Read（读取）：作用于主内存变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用；

- load（载入）：作用于工作内存的变量，它把read操作从主存中变量放入工作内存中；

- Use（使用）：作用于工作内存中的变量，它把工作内存中的变量传输给执行引擎，每当虚拟机遇到一个需要使用到变量的值，就会使用到这个指令；

- assign（赋值）：作用于工作内存中的变量，它把一个从执行引擎中接受到的值放入工作内存的变量副本中；

- store（存储）：作用于主内存中的变量，它把一个从工作内存中一个变量的值传送到主内存中，以便后续的write使用；

- write（写入）：作用于主内存中的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中；

- lock（锁定）：作用于主内存的变量，把一个变量标识为线程独占状态；

- unlock（解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定；



![image-20240330145847869](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240330145847869.png)

```java
public class Test_Volatile {
    //静态变量是类级别的变量，对于 lambda 表达式来说，它们在语义上是等同于 final 的，因为它们在整个类的生命周期中是不变的
    private  static  int num=0;
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while(num==0){
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        num=1;
        System.out.println(num);
    }
}
```

**明明已经修改了num，但程序还是卡死在那**

![image-20240330150529128](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240330150529128.png)

### Volatile

#### 可见性

```java
public class Test_Volatile {
    //静态变量是类级别的变量，对于 lambda 表达式来说，它们在语义上是等同于 final 的，因为它们在整个类的生命周期中是不变的
    private static volatile int num=0;
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while(num==0){
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        num=1;
        System.out.println(num);
    }
}
```

加上这个关键字就可以就可以实现可见性

#### 不保证原子性

当只有单个读写时，没有出现问题，但多个线程同时写呢？仅仅依靠Volatile可以保证安全吗？

> 举例来说，如果一个变量的更新操作涉及读取、修改和写入三个步骤，而这些步骤之间没有被同步控制，那么即使这个变量被声明为 `volatile`，其他线程仍然有可能在这些步骤之间插入自己的操作，导致最终结果出现异常。

```java
public class Test_Volatile2 {
    private static volatile int num = 0;
    private static void add() {
        num++;
    }
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while(Thread.activeCount() > 2) {
            //java有main和gc两个默认线程
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+" num: "+num);
    }
}
```

```
main num: 19526
```

发现 volatile 不能保证正确的相加，这说明volatile不保证原子性

我们可以通过lock和synchronized 保证正确相加

**如果不加lock和synchronized ，怎么样保证原子性？**

![image-20200812215844788](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/db6345f3804fe7e529cdb71771c8e2f9.png)

**可以使用原子类**

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/2f3fbaf1a7d0705813f1388665cebfab.png)

```java
public class Test_Volatile2 {
    private static AtomicInteger num = new AtomicInteger(0);
    private static void add() {//底层是 CAS 保证的原子性
        num.getAndIncrement();
    }
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while(Thread.activeCount() > 2) {
            //java有main和gc两个默认线程
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+" num: "+num);
    }
}
```

这些类的底层都直接和操作系统挂钩！是在内存中修改值。

```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}

public native int getIntVolatile(Object var1, long var2);
public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
```

最后都是调用的native方法

#### 禁止指令重排

**什么是指令重排？**

我们写的程序，计算机并不是按照我们自己写的那样去执行的

- 源代码
- 编译器优化重排
- 指令并行也可能会重排
- 内存系统也会重排
- 执行

**处理器在进行指令重排的时候，会考虑数据之间的依赖性！**

```java
int x=1; //1
int y=2; //2
x=x+5;   //3
y=x*x;   //4

//我们期望的执行顺序是 1234  可能执行的顺序会变成2134 1324
//可不可能是 4123？ 不可能的
```

前提：a b x y这四个值 默认都是0

| 线程A | 线程B |
| ----- | ----- |
| x=a   | y=b   |
| b=1   | a=2   |

正常的结果： **x = 0   y =0**

| 线程A | 线程B |
| ----- | ----- |
| b=1   | a=2   |
| x=a   | y=b   |

可能在线程A中会出现，先执行b=1,然后再执行x=a；

在B线程中可能会出现，先执行a=2，然后执行y=b；

那么就有可能结果如下：**x=2   y=1**

**volatile可以避免指令重排：volatile中会加一道内存的屏障，这个内存屏障可以保证在这个屏障中的指令顺序。**

内存屏障：CPU指令。

作用：

- 保证特定的操作的执行顺序；
- 可以保证某些变量的内存可见性（利用这些特性，就可以保证volatile实现的可见性）

![image-20200812220019582](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/85fa53d83ee4f89d5a7202e9e5a98987.png)

**总结**

- **volatile可以保证可见性；**
- **不能保证原子性**
- **由于内存屏障，可以保证避免指令重排的现象产生**

在哪里用这个内存屏障用得最多呢？**单例模式**

### 单例模式

单例模式是一种设计模式，它保证一个类只有一个实例，并提供一个全局访问点来访问该实例。

**在单例模式中，类的实例化过程被限制在类的内部，而外部无法直接实例化该类，只能通过特定的方法获取到该类的唯一实例。**

单例模式通常用于需要在**整个应用程序中共享一个资源或对象的场景**，例如数据库连接池、线程池、日志记录器等。通过使用单例模式，可以确保全局只有一个实例存在，从而**节省系统资源并且避免不必要的对象创建**。

单例模式的实现方式通常有以下几种

#### 饿汉式

```java
//饿汉式单例 一上来就new一个
public class Hungry {
    //当饿汉式单例有一些数组之类，一上来就创建，但没有实际使用，会浪费空间
    private byte[] data1=new byte[1024*1024];
    private byte[] data2=new byte[1024*1024];
    private byte[] data3=new byte[1024*1024];
    private byte[] data4=new byte[1024*1024];
    
    private Hungry(){}
    private static final Hungry hungry = new Hungry();

    public static Hungry getInstance(){
        return hungry;
    }
}
```

#### 懒汉式

```java
//常见懒汉式，用时再创建
public class LazyMan {
    private LazyMan() {

    }
    private static LazyMan lazyMan;

    public static LazyMan getInstance(){
        if(lazyMan == null){
            lazyMan = new LazyMan();
        }
        return lazyMan; 
    }
}
```

**有一个问题多线程时还安全吗？**

```java
public class LazyMan {
    private LazyMan() {
        System.out.println(Thread.currentThread().getName()+"我是懒汉式单例");
    }
    private static LazyMan lazyMan;

    public static LazyMan getInstance(){
        if(lazyMan == null){
            lazyMan = new LazyMan();
        }
        return lazyMan;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> LazyMan.getInstance()).start();
        }
    }
}

```

```
Thread-0我是懒汉式单例
Thread-3我是懒汉式单例
Thread-2我是懒汉式单例
Thread-1我是懒汉式单例
```

发现执行了多次，**多线程不安全！！！**因为当多个线程同时调用 `getInstance()` 方法时，它们都可以检查到 `lazyMan` 对象为 null，并且都会进入 `if (lazyMan== null)` 分支内。然后，每个线程都会创建一个 `lazyMan` 对象，并将其赋值给 `lazyMan` 变量。

#### 双重检查锁

***双重检测锁模式 简称DCL懒汉式*** 解决多线程不安全

```java
public class DCL_LazyMan {
    private DCL_LazyMan() {
        System.out.println(Thread.currentThread().getName()+"我是DCL懒汉式单例");
    }
    private static DCL_LazyMan dcl_lazyMan;

    public static DCL_LazyMan getInstance(){
        if(dcl_lazyMan == null){ //第一次检查 避免多个线程同时进入同步代码，减少性能开销
            synchronized (DCL_LazyMan.class){
                if(dcl_lazyMan == null){ //确保多线程环境下只创建一个实例
                    dcl_lazyMan = new DCL_LazyMan();
                }
            }
        }
        return dcl_lazyMan;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> DCL_LazyMan.getInstance()).start();
        }
    }
}
```

但现在仍然存在问题，之前学过 Volatile ，里面有一个指令重排的概念，我们可以发现 `dcl_lazyMan = new DCL_LazyMan();`并不是一个原子性的操作。

具体来说该行代码会如下执行:

- 1 分配内存空间
- 2 执行构造方法，初始化对象
- 3 把这个对象指向这个空间

- 这就有可能出现指令重排问题，比如执行的顺序是1 3 2 等
- 我们就可以添加volatile保证指令重排问题

双重检查锁模式在 JDK 1.5 之后才能正确工作，之前的版本存在指令重排序的问题，可能会导致多线程环境下创建多个实例的问题。通过将 `instance` 变量声明为 `volatile`，可以防止指令重排序，确保多线程环境下的安全性。

```java
private static volatile DCL_LazyMan dcl_lazyMan;
```

如果不使用 `volatile` 关键字来修饰 `instance` 对象，可能会出现以下问题：

1. **指令重排序问题**：在多线程环境下，由于指令重排序的存在，可能会导致对象的初始化过程被重排序，使得其他线程在对象尚未完全初始化完成时就获取到了未完全初始化的对象，从而导致程序出现错误。
2. **可见性问题**：当一个线程初始化了单例对象后，其他线程可能无法立即感知到该变化，导致其他线程仍然认为对象为 null，进而重复创建实例。



再思考一个问题，现在就一定安全了吗？**反射也会导致不安全！**

```java
public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    DCL_LazyMan instance = DCL_LazyMan.getInstance();
    
    Constructor<DCL_LazyMan> declaredConstructor = DCL_LazyMan.class.getDeclaredConstructor(null);
    declaredConstructor.setAccessible(true);//因为构造方法私有，所以设置可访问为true
    
    DCL_LazyMan instance2 = declaredConstructor.newInstance();
    
    System.out.println(instance);
    System.out.println(instance2);
}
```

```
main我是DCL懒汉式单例
main我是DCL懒汉式单例
com.balance.single.DCL_LazyMan@74a14482
com.balance.single.DCL_LazyMan@1540e19d
```

**发现创建了两个对象，所以反射的情况下依旧不安全**

如何解决呢？第一步，因为反射是通过构造器创建，我们在构造器方法中去判断当前是否为空，不为空抛出异常

```java
private DCL_LazyMan() {
    synchronized (DCL_LazyMan.class){
        if(dcl_lazyMan != null){
            throw new RuntimeException("不要试图使用反射破坏单例");
        }else{
            System.out.println(Thread.currentThread().getName()+"我是DCL懒汉式单例");
        }
    }
}
```

```
main我是DCL懒汉式单例
Caused by: java.lang.RuntimeException: 不要试图使用反射破坏单例
```

发现成功解决，**新的问题，我不去正常获得对象，两个对象都是反射创建会怎么样**

```java
public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    Constructor<DCL_LazyMan> declaredConstructor = DCL_LazyMan.class.getDeclaredConstructor(null);
    declaredConstructor.setAccessible(true);//因为构造方法私有，所以设置可访问为true
    //两个对象都是反射创建
    DCL_LazyMan instance = declaredConstructor.newInstance();
    DCL_LazyMan instance2 = declaredConstructor.newInstance();
    
    System.out.println(instance);
    System.out.println(instance2);
}
```

```java
main我是DCL懒汉式单例
main我是DCL懒汉式单例
com.balance.single.DCL_LazyMan@74a14482
com.balance.single.DCL_LazyMan@1540e19d
```

又不安全了，**依旧创建了两个对象**

如何解决？**我们可以创建一个标志变量，来标志是否被创建**

```java
private static boolean flag = true;
private DCL_LazyMan() {
    synchronized (DCL_LazyMan.class){
        if(flag){
            flag = false;
        }else {
            throw new RuntimeException("不要试图使用反射破坏单例");
        }
    }
}
```

```
Caused by: java.lang.RuntimeException: 不要试图使用反射破坏单例
	at com.balance.single.DCL_LazyMan.<init>(DCL_LazyMan.java:14)
	... 5 more
```

发现解决了，**但是这个字段我们也可以通过反射去获得呀，我们用反射设置这个字段不就又不安全了吗**

```java
public static void main(String[] args) throws Exception {
    Field flag1 = DCL_LazyMan.class.getDeclaredField("flag");
    flag1.setAccessible(true);
    Constructor<DCL_LazyMan> declaredConstructor = DCL_LazyMan.class.getDeclaredConstructor(null);
    declaredConstructor.setAccessible(true);//因为构造方法私有，所以设置可访问为true
    //我们在创建了一个对象之后，更改标志位
    DCL_LazyMan instance = declaredConstructor.newInstance();
    flag1.set(instance,true);

    DCL_LazyMan instance2 = declaredConstructor.newInstance();

    System.out.println(instance);
    System.out.println(instance2);
}
```

```
com.balance.single.DCL_LazyMan@1540e19d
com.balance.single.DCL_LazyMan@677327b6
```

正如我们所料，又不安全了，又创建了两个对象 

***道高一尺 魔高一丈***

究竟如何解决，看反射创建对象的 newInstance方法源码

![image-20240331110043456](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240331110043456.png)

#### 静态内部类

```java
//静态内部类实现
public class StaticSingle {
    private StaticSingle(){}

    public static StaticSingle getInstance(){
        return InnerClass.staticSingle;//使用时调用静态内部类创建的对象
    } 
    public static class InnerClass{
        private static final StaticSingle staticSingle = new StaticSingle();
    }
}
```

#### 枚举

- 枚举默认为单例模式
- 枚举本身也是一个类

```java
// 枚举默认单例模式
// 枚举本身也是一个类
public enum Single_Enum {
    //枚举的实例
    INSTANCE;

    public Single_Enum getInstance(){
        return INSTANCE;
    }
}

class Test{
    public static void main(String[] args) {
        Single_Enum single_Enum = Single_Enum.INSTANCE;
        Single_Enum single_Enum2 = Single_Enum.INSTANCE;
        System.out.println(single_Enum == single_Enum2);
    }
}
```

尝试一下用反射破坏 在target目录下查看编译后的class文件

```java
public enum Single_Enum {
    INSTANCE;

    private Single_Enum() {
    }

    public Single_Enum getInstance() {
        return INSTANCE;
    }
}
```

发现有一个默认的构造方法，反射爆破它

```java
class Test{
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Single_Enum instance = Single_Enum.INSTANCE;

        Constructor<Single_Enum> declaredConstructor = Single_Enum.class.getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);

        Single_Enum instance1 = declaredConstructor.newInstance();

        System.out.println(instance);
        System.out.println(instance1);

    }
}
```

```
Exception in thread "main" java.lang.NoSuchMethodException: com.balance.single.Single_Enum.<init>()
```

发现抛出没有此方法的异常，说明 idea 给出的 Class 文件不正确，明明没有该方法

使用 `javap -p Single_Enum.class` 去反编译一下

![image-20240331110919893](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/image-20240331110919893.png)

**说明依旧不对，java隐藏了具体实现，因为程序运行结果不会骗我们**

使用 jad 工具再次去反编译 得到真正的实现

```java
public final class EnumSingle extends Enum //可以发现枚举的确是类
{

    public static EnumSingle[] values()
    {
        return (EnumSingle[])$VALUES.clone();
    }

    public static EnumSingle valueOf(String name)
    {
        return (EnumSingle)Enum.valueOf(com/ogj/single/EnumSingle, name);
    }

    private EnumSingle(String s, int i)//这才是实际的构造方法
    {
        super(s, i);
    }

    public EnumSingle getInstance()
    {
        return INSTANCE;
    }

    public static final EnumSingle INSTANCE;
    private static final EnumSingle $VALUES[];

    static 
    {
        INSTANCE = new EnumSingle("INSTANCE", 0);
        $VALUES = (new EnumSingle[] {
            INSTANCE
        });
    }
}
```

更改反射的构造器

```java
class Test{
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Single_Enum instance = Single_Enum.INSTANCE;

        Constructor<Single_Enum> declaredConstructor = Single_Enum.class.getDeclaredConstructor(String.class,int.class);
        declaredConstructor.setAccessible(true);

        Single_Enum instance1 = declaredConstructor.newInstance();

        System.out.println(instance);
        System.out.println(instance1);

    }
}
```

```java
Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
```

**抛出了正确的异常，即 newInstance方法中说明的异常 ，不能使用反射去创建枚举对象**

### 深入理解CAS

*CAS : compareAndSet 比较并交换*

```java
public class CAS_Test {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2024);
        //boolean compareAndSet(int expect, int update)
        //期望值、更新值
        //如果实际值 和 我的期望值相同，那么就更新
        //如果实际值 和 我的期望值不同，那么就不更新
        System.out.println(atomicInteger.compareAndSet(2024, 2025));
        //因为期望值是2024  实际值却变成了2025  所以会修改失败
        //CAS 是CPU的并发原语
        System.out.println(atomicInteger.compareAndSet(2024, 2025));
    }
}
```

```
true
false
```

我们知道  `atomicInteger.getAndIncrement` 实际上调用了 unsafe 的 getAndAddInt方法

```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}
```

该方法三个参数如下

* this：当前对象

* valueOffset：**内存偏移**

  * ```java
    private static final long valueOffset;
    
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    ```

* 增加的值 也就是 1

该方法具体实现：

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

`var1` 就是调用的对象，`var2`就是内存偏移值 ,`var4`就是增加的值 1

首先调用了 `getIntVolatile` 方法，传入了调用对象，和内存偏移值

```java
public native int getIntVolatile(Object var1, long var2);
```

这是一个本地方法，实质就是计算新的地址,取新地址中的值

然后调用 `compareAndSwapInt`方法，这也是一个CAS操作，具体来说 

* 期望  var1, var2 可以计算出 var5
* 若可以 var5 + var4(1)

可以看到这是一个`do while`循环，这是自旋锁

**总结**

CAS：比较当前工作内存中的值和主内存中的值，如果这个值是期望的，那么则执行操作！如果不是就一直循环，使用的是自旋锁。

**缺点：**

- 循环会耗时；
- **一次性只能保证一个共享变量的原子性；**
- 它会**存在ABA问题**

### 原子引用

**什么是ABA问题？** 狸猫换太子

![img](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/4b9db8d951df5271f214561766442910.png)

线程1：期望值是1，要变成2；

线程2：两个操作：

- 1、期望值是1，变成3
- 2、期望是3，变成1

所以对于线程1来说，A的值还是1，所以就出现了问题，其实中间有过更改，但被隐藏了

```java
public class casDemo {
    //CAS : compareAndSet 比较并交换
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(2020);
		
        // B更改并更改回原来
        System.out.println(atomicInteger.compareAndSet(2020, 2021));
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(2021, 2020));
        System.out.println(atomicInteger.get());

		//A更改
        System.out.println(atomicInteger.compareAndSet(2020, 2021));
        System.out.println(atomicInteger.get());
    }
}

```

如何解决这个问题？ **乐观锁**

**所谓的原子引用，其实就是带版本号（乐观锁）的 原子操作！**

*Integer 使用了对象缓存机制，默认范围是-128~127，推荐使用静态工厂方法valueOf获取对象实例，而不是new，因为valueOf使用缓存，而new一定会创建新的对象分配新的内存空间。*

![image-20200812220608094](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/f0fa8dc692d7e89523bc334bebc58f15.png)

```java
public class CAS_Test {
    public static void main(String[] args) {
        //AtomicStampedReference 注意，如果泛型是一个包装类，注意对象的引用问题
        //Interger有缓存的问题
        //正常在业务操作，这里面比较的都是一个个对象
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 1);

        new Thread(() -> {
            int stamp=atomicStampedReference.getStamp();
            //获得 a1 的stamp
            System.out.println("a1=>"+stamp);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(atomicStampedReference.compareAndSet(1, 2,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            //获得 a2 的stamp
            System.out.println("a2=>"+atomicStampedReference.getStamp());

            System.out.println(atomicStampedReference.compareAndSet(2, 1,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            //获得 a3 的stamp
            System.out.println("a3=>"+atomicStampedReference.getStamp());
        }).start();

        new Thread(() -> {
            int stamp=atomicStampedReference.getStamp();
            //获得 b1 的stamp
            System.out.println("b1=>"+stamp);
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(atomicStampedReference.compareAndSet(1, 3,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1));
            //获得 b2 的stamp
            System.out.println("b2=>"+atomicStampedReference.getStamp());
        }).start();

    }
}
```

**主要就是为了判断是不是中间有更改**

### 各种锁的理解

#### 公平锁/非公平锁

* 公平锁：非常公平，不能插队，必须先来后到
* 非公平锁：不公平，允许插队，可以改变顺序

#### 可重入锁

* 可重入锁的一个主要特性是，如果一个线程已经持有了某个锁，那么它可以再次获取同一个锁，而不会导致自己被阻塞。这种特性可以使得同一个线程在访问多个同步代码块时，只需要获取一次锁，可以避免死锁的发生。

- 在可重入锁中，每个线程都维护着一个持有锁的计数器，当线程第一次获得锁时，计数器会加一，每次进入同步代码块时计数器都会递增；
- 当退出同步代码块时，计数器会递减。只有当计数器为零时，锁才会完全释放，其他线程才能获得锁。

Synchonized 锁

```java
public class Demo01 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(()->{
            phone.sms();
        },"A").start();
        new Thread(()->{
            phone.sms();
        },"B").start();
    }

}

class Phone{
    public synchronized void sms(){
        System.out.println(Thread.currentThread().getName()+"=> sms");
        call();//这里也有一把锁
    }
    public synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"=> call");
    }
}
```

Lock 锁

```java
public class Demo02 {

    public static void main(String[] args) {
        Phone2 phone = new Phone2();
        new Thread(()->{
            phone.sms();
        },"A").start();
        new Thread(()->{
            phone.sms();
        },"B").start();
    }

}
class Phone2{

    Lock lock=new ReentrantLock();

    public void sms(){
        lock.lock(); //细节：这个是两把锁，两个钥匙
        //lock锁必须配对，否则就会死锁在里面
        try {
            System.out.println(Thread.currentThread().getName()+"=> sms");
            call();//这里也有一把锁
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void call(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "=> call");
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
```

#### 自旋锁

- 自旋锁（Spin Lock）是一种非阻塞锁，它的工作方式是在获取锁时不会立即进入阻塞状态，而是采用自旋的方式反复尝试获取锁，直到成功获取为止。
- 自旋锁**适用于短时间内锁的竞争情况，当锁的持有者释放锁后，其他线程就可以立即获得锁，而无需进入阻塞状态**，从而提高了并发性能。

spin lock(自旋锁) 之前在 AtomicInteger 的 getAndIncrement 方法中就已经见识了自旋锁

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
    return var5;
}
```

设计自旋锁

```java
public class SpinlockDemo {

    // 默认
    // int 0
    //thread null
    AtomicReference<Thread> atomicReference=new AtomicReference<>();

    //加锁
    public void myLock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName()+"===> mylock");

        //自旋锁
        while (!atomicReference.compareAndSet(null,thread)){
            System.out.println(Thread.currentThread().getName()+" ==> 自旋中~");
        }
    }


    //解锁
    public void myUnlock(){
        Thread thread=Thread.currentThread();
        System.out.println(thread.getName()+"===> myUnlock");
        atomicReference.compareAndSet(thread,null);
    }

}
```

```java
public class TestSpinLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.unlock();


        //使用CAS实现自旋锁
        SpinlockDemo spinlockDemo=new SpinlockDemo();
        new Thread(()->{
            spinlockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                spinlockDemo.myUnlock();
            }
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);


        new Thread(()->{
            spinlockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                spinlockDemo.myUnlock();
            }
        },"t2").start();
    }
}
```

运行结果：

**t2进程必须等待t1进程Unlock后，才能Unlock，在这之前进行自旋**

#### 死锁

**怎么排除死锁？**

```java
package com.ogj.lock;

import java.util.concurrent.TimeUnit;

public class DeadLock {
    public static void main(String[] args) {
        String lockA= "lockA";
        String lockB= "lockB";
		//将 A B 互换位置，造成死锁现象
        new Thread(new MyThread(lockA,lockB),"t1").start();
        new Thread(new MyThread(lockB,lockA),"t2").start();
    }
}

class MyThread implements Runnable{

    private String lockA;
    private String lockB;

    public MyThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){//拿到A的锁之后继续尝试拿B的锁
            System.out.println(Thread.currentThread().getName()+" lock"+lockA+"===>get"+lockB);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+" lock"+lockB+"===>get"+lockA);
            }
        }
    }
}
```

如何解开死锁

**1、使用jps定位进程号，jdk的bin目录下： 有一个jps.exe**

![image-20200812214833647](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/32b977206fd43d9cd67cf7bf432b13e6.png)

**2、使用`jstack.exe` +进程号 找到死锁信息**

![image-20200812214920583](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/e56c37006badbc1bbbae99dba5438172.png)

**一般情况信息在最后：**

![image-20200812214957930](https://raw.githubusercontent.com/balance-hy/typora/master/thinkbook/814d63935d3d21ed799afcc2eccd20c9.png)

