import java.util.Arrays;

public class Demo1 {
    public static void main(String[] args) {
        Test test = new Test(){
            @Override
            public void say() {
                System.out.println("lalala");
            }
        };
        test.say();

    }
}
class Test{
    public Test() {
    }

    public void say(){
        System.out.println("hhhh");
    }

}