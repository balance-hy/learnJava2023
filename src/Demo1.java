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
    public void write1(){
        //注意FileWriter如果文件不存在会创建。
        String filePath="E:\\test1.txt";
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter(filePath);
            fileWriter.write('a');
            fileWriter.write("hhhh");
            fileWriter.write("我是谁",0,2);
            String str="怎么回事";
            fileWriter.write(str.toCharArray());
            fileWriter.write(str.toCharArray(),0,2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                fileWriter.close();//一定要关闭或者刷新，否则还是在内存中，并未真正写入
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


