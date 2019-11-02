package colesico.framework.example.config.single;

import colesico.framework.config.Config;

@Config
public class SingleConfig extends SingleConfigPrototype {
    private int counter = 0;

    @Override
    public String getValue() {
        return "Single" + (counter++);
    }
}
