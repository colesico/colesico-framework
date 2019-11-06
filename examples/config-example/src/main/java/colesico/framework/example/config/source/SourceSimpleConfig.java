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

    @SourceValue
    private String defaultValue ="DefaultValue";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
