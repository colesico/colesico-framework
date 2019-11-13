package colesico.framework.example.ioc.message;

public class TextMessage {
    private final String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
