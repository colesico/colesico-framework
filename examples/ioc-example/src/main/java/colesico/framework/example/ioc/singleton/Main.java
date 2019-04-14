package colesico.framework.example.ioc.singleton;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        // Build ioc instance. Ioc instance is thread safe
        final Ioc ioc = IocBuilder.forProduction();

        MySingleton1 singleton1 = ioc.instance(MySingleton1.class);
        MySingleton1 singleton11 = ioc.instance(MySingleton1.class);

        singleton1.printCounter();
        singleton11.printCounter();

        MySingleton2 singleton2 = ioc.instance(MySingleton2.class);
        MySingleton2 singleton21 = ioc.instance(MySingleton2.class);

        singleton2.printCounter();
        singleton21.printCounter();

    }
}
