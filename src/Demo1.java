import java.util.Arrays;
import java.util.Scanner;

public class Demo1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String price=scanner.nextLine();
        StringBuffer stringBuffer = new StringBuffer(price);
        int j=stringBuffer.indexOf(".");
        for(int i=3;i<j;i+=3){
            stringBuffer.insert(i,',');
            i++;
        }
        System.out.println(stringBuffer);
        scanner.close();
    }
}
