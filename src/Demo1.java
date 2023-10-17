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
    public List<Integer> findAnagrams(String s, String p) {
        HashSet<String> set = new HashSet<>();
        ArrayList<Integer> list = new ArrayList<>();
        int a[]=new int[26];
        int lengthP=p.length();
        int lengthS=s.length();
        for(int i=0;i<lengthP;i++){
            a[p.charAt(i)-'a']++;
        }
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<26;i++){
            if(a[i]!=0){
                builder.append((char)('a'+i));
                builder.append(a[i]);
            }
        }
        String key=builder.toString();
        set.add(key);//拼接的字符串作为键

        for(int i=0;i<lengthS;){
            if(i+lengthP-1>=lengthS){
                break;
            }
            int b[]=new int[26];
            for(int j=0;j<lengthP;j++){
                b[s.charAt(i+j)-'a']++;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for(int k=0;k<26;k++){//每次拼接
                if(b[k]!=0){
                    stringBuilder.append((char)('a'+k));
                    stringBuilder.append(b[k]);
                }
            }
            String key1=stringBuilder.toString();
            if(set.contains(key1)){//查是否含有
                list.add(i);
            }
            i++;
        }
        return list;
    }

}


