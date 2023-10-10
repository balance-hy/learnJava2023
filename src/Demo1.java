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
    public void homeWork() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\test.txt"));
        String Line=null;
        int i=1;
        while((Line=bufferedReader.readLine())!=null){
            System.out.println((i++)+" "+Line);
        }



        bufferedReader.close();
    }
}


