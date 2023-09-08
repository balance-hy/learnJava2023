import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Demo1 {
    enum Signal {
        // 定义一个枚举类型
        GREEN,YELLOW,RED;
    }
    public static void main(String[] args) throws ParseException {
        for(int i = 0;i < Signal.values().length;i++) {
            System.out.println("索引" + Signal.values()[i].ordinal()+"，值：" + Signal.values()[i]);
        }
    }
}
