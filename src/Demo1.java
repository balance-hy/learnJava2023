import java.util.Arrays;

public class Demo1 {

    public static void main(String[] args) {
        Alarm alarm = new Alarm();
        alarm.alarmClock(new Bell() {
            @Override
            public void ring() {
                System.out.println("test 1");
            }
        });

    }


}
interface Bell{
    void ring();
}
class Alarm{
    public void alarmClock(Bell bell){
        bell.ring();
    }
}