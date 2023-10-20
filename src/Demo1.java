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


    public static void main(String[] args){
        String s=new String("四大名著是哪些");
        byte b[];
        if(s.equals("四大名著是哪些")){
            b=(s+"红楼梦").getBytes();
        }else{
            b=("what?").getBytes();
        }
        System.out.println(new String(b));
    }
    public int[] intersection(int[] nums1, int[] nums2) {
        int nums1Len=nums1.length;
        int nums2Len=nums2.length;

        HashSet<Integer> set = new HashSet<>();
        HashSet<Integer> reSet = new HashSet<>();

        for (int i:nums1) {
            set.add(i);
        }
        for(int i:nums2){
            if(set.contains(i)){
                reSet.add(i);
            }
        }
        //方法1：将结果集合转为数组
        return reSet.stream().mapToInt(x->x).toArray();

        //方法2：另外申请一个数组存放setRes中的元素,最后返回数组
//        int[] arr = new int[reSet.size()];
//        int j = 0;
//        for(int i : reSet){
//            arr[j++] = i;
//        }
//
//        return arr;
    }

}


