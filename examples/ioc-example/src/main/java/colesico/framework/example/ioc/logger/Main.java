package colesico.framework.example.ioc.logger;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        MyBean bean = IocBuilder.forProduction().instance(MyBean.class);
        bean.run();
    }
}
