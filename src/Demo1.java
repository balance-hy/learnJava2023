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

        //加载类，返回Class类型的对象
        Class<?> aClass = Class.forName(classfullpath);
        //通过 aClass.newInstance 获得你加载的类的对象实例
        Object o = aClass.newInstance();
        //通过 aClass.getMethod 获得你加载的类的 methodName的方法对象
        // 即：在反射中，可以把方法视作对象
        Method method = aClass.getMethod(methodName);
        //通过method调用方法 ：即通过方法对象来实现调用方法
        method.invoke(o);//传统 对象名.方法(),反射 方法.invoke(对象)

        //注意 getField 无法访问到私有属性
        Field nameField = aClass.getField("name");
        System.out.println(nameField.get(o));//传统写法 对象.属性 反射 属性.get(对象)

        //默认得到无参构造
        Constructor<?> constructor = aClass.getConstructor();
        //通过指定参数的反射class，获得有参构造
        Constructor<?> constructor1 = aClass.getConstructor(String.class);
    }
}


