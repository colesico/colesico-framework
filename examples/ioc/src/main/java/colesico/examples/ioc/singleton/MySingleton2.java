package colesico.examples.ioc.singleton;

public class MySingleton2 {
    private int counter = 0;

    public void printCounter(){
        System.out.println("MySingleton2.counter="+counter);
        counter++;
    }
}
