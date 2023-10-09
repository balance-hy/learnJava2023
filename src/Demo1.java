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
    public void readMysql() throws IOException {
        Properties properties = new Properties();
        //加载指定配置文件到properties对象
        properties.load(new FileReader("src\\mysql.properties"));
        //将k-v 显示到显示器
        properties.list(System.out);
        //根据k获取v
        String user=properties.getProperty("user");
        String pwd=properties.getProperty("password");
        System.out.println(user);
        System.out.println(pwd);
    }
}


