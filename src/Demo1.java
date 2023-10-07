import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    public void objectOut() throws Exception {
        //注意此时为dat后缀，序列化后的文件格式按照他的指定来
        String filePath="D:\\data.dat";
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));

        objectOutputStream.write(100);//int-> Integer(实现了Serializable接口) 自动装箱 下面同理
        objectOutputStream.writeBoolean(false);
        objectOutputStream.writeChar('a');
        objectOutputStream.writeDouble(3.12);
        objectOutputStream.writeUTF("我是谁");//注意字符串为writeUTF

        objectOutputStream.writeObject(new Dog());//注意对象所在类需要实现Serializable接口

        objectOutputStream.close();
    }
}
class Dog implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

