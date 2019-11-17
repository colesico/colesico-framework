package colesico.framework.example.config.source;

import colesico.framework.config.Config;
import colesico.framework.config.FromSource;
import colesico.framework.config.UseSource;

@Config
@UseSource
public class SourceNestedConfig {

    @FromSource
    private NestedValue nested;

    public NestedValue getNested() {
        return nested;
    }

    public void setNested(NestedValue nested) {
        this.nested = nested;
    }
}
