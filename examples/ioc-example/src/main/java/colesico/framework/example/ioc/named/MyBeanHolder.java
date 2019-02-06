package colesico.framework.example.ioc.named;

import javax.inject.Named;

public class MyBeanHolder {

    private final MyBean defaultBean;
    private final MyBean customBean;

    public MyBeanHolder(@Named("default") MyBean defaultBean,@Named("custom") MyBean customBean) {
        this.defaultBean = defaultBean;
        this.customBean = customBean;
    }

    public void print(){
        System.out.print("Default bean: ");
        defaultBean.printHello();

        System.out.print("Custom bean: ");
        customBean.printHello();
    }
}
