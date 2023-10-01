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
        //注意FileOutputStream如果文件不存在会创建。
        String filePath="E:\\test.txt";
        FileOutputStream fileOutputStream=null;

        try {
            //默认是覆盖，构造时添加true，代表文件末尾追加
            fileOutputStream=new FileOutputStream(filePath,true);
            //方法1 写入一个字节
            fileOutputStream.write('a');
            //方法2 写入一个字符串
            String str="this is String";
            //因为write要求字符数组，用字符串的方法转化
            fileOutputStream.write(str.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}


