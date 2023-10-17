package com.balance.socket;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
