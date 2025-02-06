package colesico.framework.dslvalidator.builder;

import java.util.function.Function;

/**
 * Validated bean field reference
 *
 * @param subject   Validation subject  associated with field.Typically, field name
 * @param extractor Field value extractor
 * @param <V>       class containing field
 * @param <N>       field type
 */
public record FieldReference<V, N>(String subject, Function<V, N> extractor) {

    public static String SUBJECT_METHOD = "subject";
    public static String EXTRACTOR_METHOD = "extractor";

}
