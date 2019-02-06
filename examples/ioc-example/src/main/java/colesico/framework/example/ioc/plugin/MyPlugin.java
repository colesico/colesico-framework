package colesico.framework.example.ioc.plugin;



public class MyPlugin implements MyPluginInterface {

    @Override
    public String getInfo() {
        return "This is plugin";
    }
}
