package colesico.framework.translation;

/**
 * Defines method signature for translatable text.
 */
@FunctionalInterface
public interface Translatable {
    String translate(Object... params);
}
