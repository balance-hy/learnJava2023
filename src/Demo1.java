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
        ArrayList arrayList = new ArrayList();
        for(int i=0;i<15;i++){
            arrayList.add(i,i);
        }
        System.out.println(arrayList);
    }
}
