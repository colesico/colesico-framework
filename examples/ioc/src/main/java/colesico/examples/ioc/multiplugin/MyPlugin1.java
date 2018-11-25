package colesico.examples.ioc.multiplugin;


public class MyPlugin1 implements MyPluginInterface {


    @Override
    public String getInfo() {
        return "This is plugin #1";
    }
}
