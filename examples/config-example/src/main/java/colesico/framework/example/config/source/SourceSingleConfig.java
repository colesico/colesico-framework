package colesico.framework.example.config.source;

import colesico.framework.config.Config;
import colesico.framework.config.PropertiesSource;
import colesico.framework.config.SourceValue;
import colesico.framework.config.UseSource;

@Config
@UseSource(type = PropertiesSource.class,
    params = {PropertiesSource.FILE, "config.properties"})
public class SourceSingleConfig extends SourceSingleConfigPrototype {

    @SourceValue
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
