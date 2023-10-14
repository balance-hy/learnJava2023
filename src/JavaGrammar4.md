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
    System.out.println(inetAddress1);

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
### socket
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
        bufferedWriter.newLine();//设置结束标记，readLine读到此结束
        bufferedWriter.flush();//刷新，否则无法写入

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
