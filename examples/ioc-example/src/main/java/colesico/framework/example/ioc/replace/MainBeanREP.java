package colesico.framework.example.ioc.replace;

public class MainBeanREP {
    private final BeanInterface plugin;

    public MainBeanREP(BeanInterface plugin) {
        this.plugin = plugin;
    }

    public String getPluginInfo() {
        return plugin.getInfo();
    }
}
