package colesico.framework.example.ioc.plugin;

import colesico.framework.ioc.IocBuilder;

public class Main {

    public static void main(String[] args) {
        MyPluginInterface plugin = IocBuilder.forProduction().instance(MyPluginInterface.class);
        System.out.println(plugin.getInfo());
    }
}
