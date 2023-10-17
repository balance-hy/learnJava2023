## ==和equals对比
==:
1. 既可以判断基本类型，又可以判断引用类型
2. 如果判断基本类型，判断其值是否相等
3. 如果判断引用类型，判断的是地址是否相等，即判定是否为同一对象  

只要有基本数据类型，判断的便是值是否相等

equals:
1. 是Object类的方法,只能判断引用类型
2. 默认判断的是地址是否相等，子类往往重写该方法，用于判断内容是否相等，比如Integer，String

## comparator和comparable
> https://cloud.tencent.com/developer/article/1918856


## socket问题
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310171459621.PNG)  
while循环出错
```java
public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("name");
        bufferedWriter.newLine();
        bufferedWriter.flush();

        //接受回应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        while((s=bufferedReader.readLine())!=null){
            System.out.println(s);
        }
        //关闭
        bufferedReader.close();
        bufferedWriter.close();
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

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String s;
        while ((s=bufferedReader.readLine())!="\n") {

        }
        if(s.equals("name")){
            bufferedWriter.write("我是nova");
        }else if(s.equals("hobby")){
            bufferedWriter.write("Java");
        }else{
            bufferedWriter.write("什么啊");
        }
        System.out.println(s);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        //关闭
        bufferedWriter.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();
    }
}
```
## 由下载文件编程引出的问题
![](https://raw.githubusercontent.com/balance-hy/typora/master/2023img/202310171724458.PNG)  
```java
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(),9999);
        byte a[]="最长的电影".getBytes();


        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedOutputStream.write(a);
        bufferedOutputStream.flush();
        socket.shutdownOutput();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream bufferedOutputStream1 = new BufferedOutputStream(new FileOutputStream("D:\\1.wma"));
        byte b[]=new byte[1024];
        int len=0;
        while((len=bufferedInputStream.read(b))!=-1){
            bufferedOutputStream1.write(b,0,len);
        }
        bufferedOutputStream1.flush();



        bufferedOutputStream1.close();
        bufferedInputStream.close();
        bufferedOutputStream.close();
        socket.close();

    }
}

public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket=serverSocket.accept();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        byte a[]=new byte[1024];
        int len=0;
        String path="";
        while((len=bufferedInputStream.read(a))!=-1){
            path+=new String(a,0,len);
        }
        System.out.println("当前想下载 "+path);

        String resFile="";
        if("最长的电影".equals(path)){
            resFile="D:\\"+path+".wma";
        }else{
            resFile="D:\\晴天.wma";
        }
        System.out.println("当前下载的是 "+resFile);
        BufferedInputStream bufferedInputStream1 = new BufferedInputStream(new FileInputStream(resFile));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

        byte b[]=new byte[1024];
        while((len=bufferedInputStream1.read(b))!=-1){
            bufferedOutputStream.write(b,0,len);
        }
        bufferedOutputStream.flush();
        socket.shutdownOutput();


        bufferedInputStream1.close();
        bufferedOutputStream.close();
        bufferedInputStream.close();
        socket.close();
        serverSocket.close();

    }
}
```
### 需不需要flush
字节流不需要 flush 操作，因为字节流直接操作的是字节，中途不需要做任何转换，所以直接就可以操作文件。  
这里注意如果使用BufferedInputStream/BufferedOutputStream，它本身就带缓冲区，所以必须显式flush。  
而字符流，说到底，**其底层还是字节流**，但是字符流帮我们将字节转换成了字符，这个转换需要依赖字符表，所以就需要在字符和字节完成转换之后通过 flush 操作刷到磁盘中。  
需要注意的是，字节输出流最顶层类 OutputStream 中也提供了 flush 方法，但是它是一个空的方法，如果有子类有需要，也可以实现 flush 方法。

### BufferedOutputStream缓冲区
BufferedOutputStream 的缓冲区大小是可以设置的，但它是有限的，不会动态增加。缓冲区的大小通常由构造函数的参数确定，你可以在创建 BufferedOutputStream 对象时指定缓冲区大小。  
例如，你可以这样创建一个具有 8192 字节（8KB）缓冲区的 BufferedOutputStream：
```java
BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 8192);
```
缓冲区的大小决定了每次写入的数据块大小。当写入的数据超过缓冲区大小时，BufferedOutputStream 会将数据刷新到底层的输出流。   
选择适当大小的缓冲区可以提高文件写入的效率。太小的缓冲区可能导致频繁的刷新操作，而太大的缓冲区可能浪费内存。  
常见的缓冲区大小是 4KB、8KB 或 16KB，但具体的大小可以根据性能需求进行调整。

### 当写入字节超出缓冲区会如何
注意，一旦创建 BufferedOutputStream 对象，你通常不能动态更改其缓冲区大小。如果需要不同大小的缓冲区，你需要创建新的 BufferedOutputStream 对象。  
BufferedOutputStream 的缓冲区**默认大小为 8KB**，但这不意味着每次写入超过 8KB 的数据都会导致问题。**缓冲区的主要目的是优化磁盘写入，减少频繁的磁盘访问，提高性能**。   
当你在循环内部多次写入数据时，缓冲区会在达到其容量限制之前积累数据。然后，当缓冲区满了，flush() 方法会被调用，将数据刷新到底层的输出流中。这可以减少磁盘访问的次数，提高效率。
因此，即使你的写入超过了 8KB，**BufferedOutputStream 会自动管理缓冲区并确保数据定期刷新到输出流中**。  
在循环外部调用 flush() 也是一种有效的方式，它会立即刷新缓冲区中的数据，但通常在循环内部积累一定量的数据后才进行刷新可以提高性能。  
如果你在循环外部使用 flush()，也不会出现问题，因为它只会刷新缓冲区中的数据，而不会导致缓冲区溢出。缓冲区的设计目的是在适当的时机将数据刷新到输出流，而不是让缓冲区无限增长。

### 如果不显示写flush会出什么问题
当你注释掉了 bufferedOutputStream1.flush()和bufferedOutputStream.flush()，文件的内容可能会缺少几KB的数据是因为缓冲区内的数据未被完全刷新到磁盘。  
BufferedOutputStream 的主要作用是将数据先缓存到内存中，以减少频繁的磁盘写入操作，提高性能。  
当你调用 write() 方法写入数据时，数据被写入缓冲区，但不一定会立即刷新到磁盘。缓冲区会等待到达一定大小或者在关闭流时才将数据刷新到磁盘。  
在你的情况下，如果在没有显式调用 flush() 的情况下关闭 BufferedOutputStream 或 Socket 连接，缓冲区中的数据可能没有完全刷新到磁盘，导致文件缺少一些数据。  
为确保数据被完全刷新到磁盘，建议在适当的时候调用 flush() 或在关闭 BufferedOutputStream 和 Socket 连接之前，确保数据被正确刷新。这可以通过以下方式之一来实现：  

* 在写入完数据后显式调用 flush()，如你之前提到的。
* 在关闭 BufferedOutputStream 之前调用 flush()。
* 在关闭 Socket 连接之前调用 flush()。
* 这将确保数据被完全刷新到磁盘，避免数据丢失。因此，在文件传输过程中，最好显式地调用 flush() 来确保数据的完整性。

Java 的 I/O 类库会在关闭流或连接时自动执行缓冲区的刷新操作以确保数据被正确发送。这是一种安全机制，通常情况下会避免数据丢失。  
然而，在某些情况下，可能会出现数据缺失的情况，其中一种可能性是因为你在写入数据时使用了 write() 方法，但数据的大小没有达到缓冲区的刷新阈值。这导致部分数据仍然在缓冲区中，而没有被刷新到磁盘。 
虽然 Java 库会尽力确保数据被刷新，但为了确保数据的完整性，特别是在某些特定情况下，最好显式调用 flush() 来触发缓冲区的刷新，以确保数据被写入磁盘。这是为了处理一些特殊情况，以确保数据不会在关闭流时丢失。  
所以，尽管 Java 通常会自动刷新缓冲区，但在涉及文件传输等关键操作时，显式调用 flush() 作为一种最佳实践可以提高代码的可靠性。
## BufferedInputStream/BufferedOutputStream包装流关闭引发的异常
BufferedInputStream/BufferedOutputStream在关闭后，不仅仅可以关闭里面的节点流，还能关闭socket。  
因此网络编程用到包装流时，buffered相关操作的close()最好放到最后。
遇到socket is closed问题时可以考虑是这一原因导致的。
## 服务器和客户端之间使用BufferedReader的readline阻塞问题
问题代码如下：  
```java
public class SocketClient {
    public static void main(String[] args) throws IOException {
        //连接本机的9999端口，若连接成功，返回socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //连接上后，通过socket.getOutputStream获取到关联的输出流对象
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("name");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        socket.shutdownOutput();//此处应设置输出结束标志
        //接受回应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        while((s=bufferedReader.readLine())!=null){
            System.out.println(s);
        }
        //关闭
        bufferedReader.close();
        bufferedWriter.close();
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

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String s;
        while ((s=bufferedReader.readLine())!=null) {
            bufferedWriter.write("我是nova");
            System.out.println(s);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        //关闭
        bufferedWriter.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();
    }
}
```
问题分析：
（1）BufferedReader的readLine方法只有在遇到流结尾或者流关闭了才会返回null  
（2）对于读取文件流，当读到文件的结尾时，就是到了流的结尾  
（3）但对于socket，不能认为把某次写入到流中的数据读取完了就算流结尾了，但是socket流还存在，还可以继续往里面写入数据然后再读取。所以用BufferedReader封装socket的输入流，调用BufferedReader的readLine方法是不会返回null的  

## ？

