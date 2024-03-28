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

**为什么？ 如果你认为是顺序在前？ 这个答案是错误的！**

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
- 它**通过分段锁（Segment Locking）来实现并发访问，即将整个哈希表分成多个段（Segment），每个段拥有自己的锁。**
- 在并发环境下，`ConcurrentHashMap` 提供了较好的性能和可伸缩性，多个线程可以同时读取、更新、插入和删除元素而不需要额外的同步措施。
- 由于 `ConcurrentHashMap` 的设计，不会抛出 `ConcurrentModificationException` 异常，因为它允许在迭代过程中修改集合。

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

await 等待计数器归零，就唤醒，再继续向下运行

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
