package colesico.examples.eventbus;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        Ioc ioc = IocBuilder.forDevelopment();
        Sender sender = ioc.instance(Sender.class);
        sender.sendEvent();

    }
}
