package colesico.framework.translation;

/**
 * Text formatter for pattern params substitution.
 * Default implementation with {@link java.text.MessageFormat#format(String, Object...)}
 */
public interface TextFormatter {

    String format(String pattern, Object... params);

}
