package com.balance.socket;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
