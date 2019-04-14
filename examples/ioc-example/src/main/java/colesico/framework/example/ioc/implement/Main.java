package colesico.framework.example.ioc.implement;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        MyInterface myImpl = IocBuilder.forProduction().instance(MyInterface.class);
        myImpl.printHello();

    }
}
