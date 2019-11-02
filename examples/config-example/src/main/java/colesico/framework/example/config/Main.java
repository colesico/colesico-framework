package colesico.framework.example.config;

import colesico.framework.ioc.IocBuilder;
import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        MainBean service = IocBuilder.get().build().instance(MainBean.class);

        out.println("Value from simple config: "+service.getSimpleConfigValue());
        out.println("Value from single config: "+service.getSingleConfigValue());
        out.println("Values from polyvariant configs: "+service.getPolyconfigValues());
        out.println("Values from message configs: "+service.getMessageConfigValues());
        out.println("Value from simple source config: "+service.getSourceSimpleConfigValue());
        out.println("Value from single source config: "+service.getSourceSingleConfigValue());
    }
}
