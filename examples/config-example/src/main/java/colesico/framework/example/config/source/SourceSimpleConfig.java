package colesico.framework.example.config.source;

import colesico.framework.config.Config;
import colesico.framework.config.PropertiesSource;
import colesico.framework.config.SourceValue;
import colesico.framework.config.UseSource;

@Config
@UseSource(type = PropertiesSource.class)
public class SourceSimpleConfig {

    @SourceValue
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
