package colesico.framework.translation.internal;

import colesico.framework.translation.TextFormatter;

import jakarta.inject.Singleton;
import java.text.MessageFormat;

@Singleton
public class TextFormatterImpl implements TextFormatter {

    @Override
    public String format(String pattern, Object... params) {
        return MessageFormat.format(pattern, params);
    }
}
