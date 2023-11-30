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


