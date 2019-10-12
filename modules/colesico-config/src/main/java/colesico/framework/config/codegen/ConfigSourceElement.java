package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassType;

import java.util.ArrayList;
import java.util.List;

public class ConfigSourceElement {
    private final ClassType driver;
    private final String[] params;

    private final List<SourceValueElement> sourceValues = new ArrayList<>();

    public ConfigSourceElement(ClassType driver, String[] params) {
        this.driver = driver;
        this.params = params;
    }

    public void addSourceValue(SourceValueElement sv) {
        sourceValues.add(sv);
    }

    public ClassType getDriver() {
        return driver;
    }

    public String[] getParams() {
        return params;
    }

    public List<SourceValueElement> getSourceValues() {
        return sourceValues;
    }
}
