package colesico.examples.ioc.singleton;

import javax.inject.Singleton;

@Singleton
public class MySingleton1 {

    private int counter = 0;

    public void printCounter(){
        System.out.println("MySingleton1.counter="+counter);
        counter++;
    }

}
