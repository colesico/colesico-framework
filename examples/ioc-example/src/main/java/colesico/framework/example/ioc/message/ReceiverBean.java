package colesico.framework.example.ioc.message;

import colesico.framework.ioc.Message;

public class ReceiverBean {
    private final TextMessage message;

    public ReceiverBean(@Message TextMessage message) {
        this.message = message;
    }

    public TextMessage getMessage() {
        return message;
    }
}
