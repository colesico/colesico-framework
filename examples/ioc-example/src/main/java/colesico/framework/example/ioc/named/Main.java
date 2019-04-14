package colesico.framework.example.ioc.named;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {

        MyBeanHolder beanHolder = IocBuilder.forDevelopment().instance(MyBeanHolder.class);
        beanHolder.print();
    }
}
