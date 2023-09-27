import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
    public void delete1(){
        File file = new File("D:\\this.txt");
        if(file.exists()){
            if(file.delete()){
                System.out.println("文件删除成功");
            }else{
                System.out.println("文件删除失败");
            }
        }else{
            System.out.println("文件不存在");
        }

        File file1 = new File("D:\\this");
        if(file1.exists()){
            if(file1.delete()){
                System.out.println("目录删除成功");
            }else{
                System.out.println("目录删除失败");
            }
        }else{
            System.out.println("目录不存在");
        }

    }
}


