package colesico.framework.example.config.message;

import colesico.framework.config.Config;
import colesico.framework.config.DefaultMessage;

@Config
@DefaultMessage
public class MessageConfigDefault extends MessageConfigPrototype {
    @Override
    public String getValue() {
        return "MessageDefault";
    }
}
