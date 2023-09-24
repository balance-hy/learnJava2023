import org.junit.jupiter.api.Test;

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
        Dog dog = new Dog();
        //dog.start();//报错，无start方法
        //dog.run();//正确，但这时main线程，并未多创建线程
        Thread thread = new Thread(dog);
        thread.start();
    }
}
class Dog implements Runnable{
    int times=0;
    @Override
    public void run() {
        while(true){
            System.out.println("this is a dog and is "+Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            times++;
            if(times==10){
                break;
            }
        }
    }
}


