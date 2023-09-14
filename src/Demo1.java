import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Demo1 {

    public static void main(String[] args) throws ParseException {
        Map map = new HashMap();
        map.put("no1",1);
        map.put("no2",2);
        map.put("no3",3);
        map.put("no4",4);
        map.put("no5",5);
        map.put("no6",6);
        //第一组：先取出所有的Key，通过Key取Value
        Set set = map.keySet();
        //1.增强for:
        for (Object key:set) {
            System.out.println(key+":"+map.get(key));
        }
        //2.迭代器
        System.out.println("=================");
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object key =  iterator.next();
            System.out.println(key+":"+map.get(key));
        }
        //第二组：直接通过Values取出所有Value
        Collection values = map.values();
        //1.增强for 略
        //2.迭代器  略

        //第三组: 通过EntrySet来获取
        //1.增强for
        System.out.println("=================");
        Set set1 = map.entrySet();//EntrySet里面是Map.Entry
        for (Object entry:set1) {
            //所以此处需要先转换
            Map.Entry m=(Map.Entry)entry;
            System.out.println(m.getKey()+":"+m.getValue());//用Entry的方法getKey和getValue
        }
        //2.迭代器
        System.out.println("=================");
        Iterator iterator1 = set1.iterator();
        while (iterator1.hasNext()) {
            Object next =  iterator1.next();//本质是HashMap$Node，但若转换为Node，没有相应方法取键值
            Map.Entry entry=(Map.Entry)next;//所以转换为Entry
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
