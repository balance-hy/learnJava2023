import com.balance.udp.udpReceive;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


    public static void main(String[] args) throws Exception {
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

    }
}


