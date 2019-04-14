package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {
        MyHostBean bean = IocBuilder.forProduction().instance(MyHostBean.class);
        bean.run();
    }
}
