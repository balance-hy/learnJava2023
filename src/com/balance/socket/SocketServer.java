package com.balance.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
