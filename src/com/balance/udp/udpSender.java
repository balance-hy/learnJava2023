package com.balance.udp;

import java.io.IOException;
import java.net.*;

public class udpSender {
    public static void main(String[] args) throws IOException {
        //指定接收的端口
        DatagramSocket datagramSocket = new DatagramSocket(9998);
        byte a[]="四大名著是哪些".getBytes();//构建发送数组
        //构建数据报
        DatagramPacket datagramPacket = new DatagramPacket(a,a.length, InetAddress.getByName("10.20.72.140"),9999);
        datagramSocket.send(datagramPacket);//发送数据报

        //接收数据报
        byte b[]=new byte[1024];
        datagramPacket = new DatagramPacket(b, b.length);
        System.out.println("准备接受数据");
        datagramSocket.receive(datagramPacket);
        System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getLength()));
        //关闭资源
        datagramSocket.close();
    }
}
