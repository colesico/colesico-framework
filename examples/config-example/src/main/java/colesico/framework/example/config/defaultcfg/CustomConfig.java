package colesico.framework.example.config.defaultcfg;

import colesico.framework.config.Config;

/**
 * App custom config
 */
@Config
public class CustomConfig extends DefaultConfigPrototype {

    @Override
    public String configure() {
        System.out.println("Custom configuration");
        return "custom";
    }
}
