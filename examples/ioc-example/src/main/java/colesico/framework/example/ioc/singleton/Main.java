package colesico.framework.example.ioc.singleton;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        // Setup ioc builder
        // IocBuilder is not thread safe
        IocBuilder iocBuilder = IocBuilder.get();

        // Build ioc instance. Ioc instance is thread safe
        final Ioc ioc = iocBuilder.build();


        MySingleton1 sing1_0 = ioc.instance(MySingleton1.class);
        MySingleton1 sing1_1 = ioc.instance(MySingleton1.class);

        sing1_0.printCounter();
        sing1_1.printCounter();

        MySingleton2 sing2_0 = ioc.instance(MySingleton2.class);
        MySingleton2 sing2_1 = ioc.instance(MySingleton2.class);


        sing2_0.printCounter();
        sing2_1.printCounter();

    }
}
