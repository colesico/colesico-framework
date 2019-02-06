package colesico.framework.example.ioc.plugin;

public class MyStubBean implements MyPluginInterface {

    @Override
    public String getInfo() {
        return "This is stub";
    }
}
