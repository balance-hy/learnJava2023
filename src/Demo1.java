import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Demo1 {

    public static void main(String[] args){
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee("hy",10000,new MyDate(12,1,2020)));
        employees.add(new Employee("zl",20000,new MyDate(1,9,2010)));
        employees.add(new Employee("zl",20000,new MyDate(2,9,2010)));
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                if(o1.getName().compareTo(o2.getName())!=0){
                    return o1.getName().compareTo(o2.getName());
                }
                if(o1.getBirthday().getYear()-o2.getBirthday().getYear()==0){
                    if(o1.getBirthday().getMonth()-o2.getBirthday().getMonth()==0){
                        return o1.getBirthday().getDay()-o2.getBirthday().getDay();
                    }else{
                        return o1.getBirthday().getMonth()-o2.getBirthday().getMonth();
                    }
                }else{
                    return o1.getBirthday().getYear()-o2.getBirthday().getYear();
                }
            }
        });
        System.out.println(employees);

    }

}
class MyDate{
    private int month;
    private int day;
    private int year;

    public MyDate(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "month=" + month +
                ", day=" + day +
                ", year=" + year +
                '}';
    }

}
class Employee{
    private String name;
    private Integer sal;
    private MyDate birthday;

    public Employee(String name, Integer sal, MyDate birthday) {
        this.name = name;
        this.sal = sal;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    public MyDate getBirthday() {
        return birthday;
    }

    public void setBirthday(MyDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "\nEmployee{" +
                "name='" + name + '\'' +
                ", sal=" + sal +
                ", birthday=" + birthday +
                '}';
    }
}
