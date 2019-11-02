package colesico.framework.example.config.message;

import colesico.framework.config.Config;

@Config
public class MessageConfig1 extends MessageConfigPrototype {
    @Override
    public String getValue() {
        return "Message1";
    }
}
