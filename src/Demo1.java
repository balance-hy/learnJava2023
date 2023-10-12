import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Demo1 {


    public static void main(String[] args) throws InterruptedException {

    }
    @Test
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
}


