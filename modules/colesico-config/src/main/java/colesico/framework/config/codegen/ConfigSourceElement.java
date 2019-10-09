package colesico.framework.config.codegen;

import colesico.framework.assist.codegen.model.ClassType;

public class ConfigSourceElement {
    private final ClassType driver;
    private final String uri;

    public ConfigSourceElement(ClassType driver, String uri) {
        this.driver = driver;
        this.uri = uri;
    }

    public ClassType getDriver() {
        return driver;
    }

    public String getUri() {
        return uri;
    }
}
