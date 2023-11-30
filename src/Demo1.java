import com.balance.udp.udpReceive;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\pythonCode\\demo1\\utils\\solve.txt");
        int[][] inEqual =new int[324][9];//不等式系数数组
        readFile(file,inEqual);//获得不等式系数
        int[][] DDT=new int[16][16];//DDT数组
        Scanner scanner = new Scanner(System.in);
        int sum0=0;
        for (int i = 0; i < 16; i++) {//获得DDT
            //读取整行数据
            String line = scanner.nextLine();
            //将每个整数分割保存到一维数组
            String[] values = line.split("\\s+");
            //创建长度为 n 的一维数组
            DDT[i] = new int[16];
            //保存每个整数到一维数组
            for (int j = 0; j < 16; j++) {
                DDT[i][j] = Integer.parseInt(values[j]);
                if(DDT[i][j]==0){
                    sum0++;
                }
            }
        }
        scanner.close();
        System.out.println("条目为0的个数:"+" "+sum0);
        int[][] R=new int[sum0][324];//获得不可能差分被不等式排除数组
        solveR(inEqual,DDT,R);
        printArray(R);
    }
    public static void readFile(File file, int[][] inEqual) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        int sum=0;
        while((line=bufferedReader.readLine())!=null){
            String[] line1=line.substring(line.indexOf('(')+1,line.indexOf(')')).split(",");//截取不等式系数
            for (int i = 0; i < line1.length; i++) {
                inEqual[sum][i]=Integer.parseInt(line1[i].trim());//去前后空格再转换
            }
            String[] line2 = line.substring(line.indexOf('x')).split(" ");
            inEqual[sum][line1.length]=Integer.parseInt(line2[2]);
            sum++;
        }
        System.out.println("不等式有"+" "+sum+" "+"个");
    }

    //对于每一个由SageMath生成的不等式，检验不可能差分是否可以由这个不等式移除
    public static void solveR(int[][] inEqual,int[][] DDT,int[][] R){
        int x0,x1,x2,x3,y0,y1,y2,y3,c;
        int iR=0,jR=0;
        for (int i = 0; i < inEqual.length; i++) {
                x0=inEqual[i][0];
                x1=inEqual[i][1];
                x2=inEqual[i][2];
                x3=inEqual[i][3];
                y0=inEqual[i][4];
                y1=inEqual[i][5];
                y2=inEqual[i][6];
                y3=inEqual[i][7];
                c=inEqual[i][8];
            for (int j = 0; j < DDT.length; j++) {
                for (int k = 0; k < DDT[j].length; k++) {
                    if(DDT[j][k]==0){
                        //横纵坐标转为2进制，然后对不等式进行判断
                        String HC=toBinary(j);// Horizontal coordinate 横坐标
                        String VC=toBinary(k);// Vertical coordinate 纵坐标
                        int HC1 = Integer.parseInt(java.lang.String.valueOf(HC.charAt(0))) * x0 + Integer.parseInt(java.lang.String.valueOf(HC.charAt(1)))* x1 + Integer.parseInt(java.lang.String.valueOf(HC.charAt(2))) * x2 + Integer.parseInt(java.lang.String.valueOf(HC.charAt(3))) * x3;
                        int VC1=Integer.parseInt(java.lang.String.valueOf(VC.charAt(0)))*y0+Integer.parseInt(java.lang.String.valueOf(VC.charAt(1)))*y1+Integer.parseInt(java.lang.String.valueOf(VC.charAt(2)))*y2+Integer.parseInt(java.lang.String.valueOf(VC.charAt(3)))*y3;
                        if(HC1+VC1+c >=0){
                            R[iR][jR]=0;//如果满足，说明不等式无法排除这个不可能差分，置0
                        }else {
                            R[iR][jR]=1;
                        }
                        iR++;//按列更新，先是第一列....第n列
                    }
                }
            }
            iR=0;
            jR++;
        }
    }
    //十进制转二进制，且补0
    public static String toBinary(int i){
        StringBuilder binary= new StringBuilder();
        while(i>0){
            int remain=i%2;
            binary.insert(0, remain);
            i/=2;
        }
        while(binary.length()<4){
            binary.insert(0, 0);
        }
        return binary.toString();
    }
    //打印数组
    public static void printArray(int[][] A){
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.print(A[i][j]+" ");
            }
            System.out.println();
        }
    }
    @Test
    public void test(){
//        int i=0;
//        String binary="";
//        while(i>0){
//            int remain=i%2;
//            binary=remain+binary;
//            i/=2;
//        }
//        while(binary.length()<4){
//            binary=0+binary;
//        }
//        System.out.println(binary);
    }
}





