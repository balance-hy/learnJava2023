import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Demo1 {
    public static void main(String[] args) throws ParseException {
        Integer i[]={99,11,-1,100,6};
        List<Integer> asList=Arrays.asList(i);
        System.out.println(asList);

        String s="1996年01月01日 10:20:30 星期一";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss E");
        String format = simpleDateFormat.format(date);
        Date parse=simpleDateFormat.parse(s);
    }
}
