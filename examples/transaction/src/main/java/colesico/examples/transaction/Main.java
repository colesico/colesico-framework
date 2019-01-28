package colesico.examples.transaction;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        MyService myService= IocBuilder.forDevelopment().instance(MyService.class);
        myService.create("create");

        myService.save("save");
        myService.delete("delete");
        myService.update("update");
        myService.update2("update2");

    }
}
