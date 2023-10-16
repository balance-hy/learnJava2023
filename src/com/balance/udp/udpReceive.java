package com.balance.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class udpReceive {
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(9999);
        //UDP包最大为64k=64*1024，按需构造大小
        byte a[]=new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(a, a.length);
        System.out.println("准备接受数据");
        datagramSocket.receive(datagramPacket);//未接收到数据会阻塞

        //输出接收到的数据  datagramPacket.getData()实际接收到的数据 datagramPacket.getLength()实际接收到的数据长度
        System.out.println(new String(datagramPacket.getData(),0,datagramPacket.getLength()));

        //输出数据报
        byte b[]="ok".getBytes();
        datagramPacket = new DatagramPacket(b, b.length, InetAddress.getByName("10.20.108.9"), 9998);
        datagramSocket.send(datagramPacket);

        //关闭资源
        datagramSocket.close();
    }
}
