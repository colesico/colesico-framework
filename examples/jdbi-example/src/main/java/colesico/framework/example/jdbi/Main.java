package colesico.framework.example.jdbi;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        MyService srv = IocBuilder.forDevelopment().instance(MyService.class);
        System.out.println("Value from DB = "+srv.readValue(1));
    }
}
