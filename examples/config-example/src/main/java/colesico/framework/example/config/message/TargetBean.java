package colesico.framework.example.config.message;

import colesico.framework.ioc.Message;

/**
 * A message based configurable service
 */
public class TargetBean {

    private final MessageConfigPrototype config;

    public TargetBean(@Message MessageConfigPrototype config) {
        this.config = config;
    }

    public String getValue() {
        return config.getValue();
    }
}
