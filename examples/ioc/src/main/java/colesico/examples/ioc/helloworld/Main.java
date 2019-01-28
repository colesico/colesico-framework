package colesico.examples.ioc.helloworld;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        // Setup ioc builder
        // IocBuilder is not thread safe
        IocBuilder iocBuilder = IocBuilder.get();

        // Build ioc instance. Ioc instance is thread safe
        final Ioc ioc = iocBuilder.build();

        // Create our service holder instance.
        MyServiceHolder msh = ioc.instance(MyServiceHolder.class);
        msh.run();

    }
}