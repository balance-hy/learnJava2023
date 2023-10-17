package com.balance.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
