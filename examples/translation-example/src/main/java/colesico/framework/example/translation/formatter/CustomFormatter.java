package colesico.framework.example.translation.formatter;

import colesico.framework.translation.TextFormatter;
import jakarta.inject.Singleton;

import java.text.MessageFormat;

@Singleton
public class CustomFormatter implements TextFormatter {

    private String text;

    @Override
    public String format(String pattern, Object... params) {
        text = MessageFormat.format(pattern, params);
        return text;
    }

    public String getText() {
        return text;
    }
}
